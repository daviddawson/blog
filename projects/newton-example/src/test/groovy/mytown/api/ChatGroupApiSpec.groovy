package mytown.api

import io.muoncore.Muon
import io.muoncore.eventstore.TestEventStore
import io.muoncore.protocol.reactivestream.client.ReactiveStreamClient
import io.muoncore.protocol.reactivestream.client.StreamData
import io.muoncore.protocol.rpc.Request
import io.muoncore.protocol.rpc.Response
import io.muoncore.protocol.rpc.client.RpcClient
import mytown.BackEndApplication
import mytown.MuonTestConfig
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import spock.lang.Shared
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import static mytown.api.Fixtures.createGroup
import static mytown.api.Fixtures.derek
import static mytown.api.Fixtures.jenny
import static mytown.api.Fixtures.user

@ActiveProfiles("test")
@SpringBootTest(classes = [MuonTestConfig, BackEndApplication])
@DirtiesContext
class ChatGroupApiSpec extends Specification {

    @Autowired
    TestEventStore eventStore

    @Autowired
    Muon muon

    RpcClient rpc
    ReactiveStreamClient rx

    def setup() {
        rpc = new RpcClient(muon)
        rx = new ReactiveStreamClient(muon)
        def create = Fixtures.createUser(rpc)

        create(derek())
        create(jenny())
    }

    def cleanup() {
//        eventStore.history.clear()
    }


    /*

    create a chat
    add a user to a chat
    remove a user

    get the list of chats containing a user
     - rpc
     - stream

    add a message to a chat /chat/message

    subscribe to a chat - stream

     */


    def "/chat/list - rpc"() {
        createGroup(rpc)(derek())
        createGroup(rpc)(derek())
        createGroup(rpc)(derek())

        def req = new Request(new URI("rpc://back-end/chat/list"), [], derek())

        def response = rpc.request(req).get()

        when:
        def ret = response.getPayload(List)

        then:
        ret.size() > 1
        with(ret[0]) {
            it.channel_id
            it.user_id.size() > 0
            it.type in ["friend", "group"]
        }
    }

    def "/chat/list - streaming"() {

        def data

        rx.subscribe(new URI("stream://back-end/chat/list?token=${derek().token}"), new Subscriber<StreamData>() {
            @Override
            void onSubscribe(Subscription subscription) { subscription.request(Integer.MAX_VALUE) }

            @Override
            void onNext(StreamData streamData) {
                data = streamData.getPayload(List)
            }

            @Override
            void onError(Throwable throwable) {

            }

            @Override
            void onComplete() {

            }
        })

        createGroup(rpc)(derek())

        expect:
        new PollingConditions().eventually {
            data
            data[0].channel_id
            data[0].user_id[0] == "random"
        }
    }

//    def "/chat/status - rpc"() {
//        def req = new Request(new URI("rpc://back-end/chat/status"), [], derek())
//
//        def response = rpc.request(req).get()
//
//        when:
//        def ret = response.getPayload(List)
//
//        then:
//        ret.size() > 1
//        with(ret[-1]) {
//            it.channel_id
//            it.messages.size() > 0
//            it.messages[0].id
//            it.messages[0].text
//        }
//    }

    def "/chat/message - rpc"() {

        def channel_id = createGroup(rpc)(derek()).getPayload(Map).channel_id

        Response response = postmessage(channel_id)

        when:
        def ret = response.getPayload(String)
        println ret
        then:
        ret == "posted"
    }

    private Response postmessage(channel_id) {
        def msg = ["channel_id": channel_id,
                   "message"   : [
                           "user"        : [
                                   "name"   : user(derek()).first,
                                   "user_id": user(derek()).id
                           ],
                           "text"        : "Lets Go Shopping!",
                           "type"        : "message",
                           "message_type": "chat"
                   ]]

        def req = new Request(new URI("rpc://back-end/chat/message"), msg, derek())

        def response = rpc.request(req).get()
        response
    }

    def "/chat/create - rpc"() {
        def ret = createGroup(rpc)(derek())

        expect:
        ret.getPayload(Map).channel_id
    }

    def "/chat/addUser - rpc"() {
        def ret = createGroup(rpc)(derek()).getPayload(Map)

        def channelId = ret.channel_id

        def req = new Request(new URI("rpc://back-end/chat/addUser"), [channel_id: channelId, user: user(jenny()).id], derek())

        def response = rpc.request(req).get()
        sleep 100

        def group =  rpc.request(new Request(new URI("rpc://back-end/chat/list"), [], derek())).get().getPayload(List)
        println group

        expect:
        group.find { it.channel_id == channelId }.user_id.contains(user(jenny()).id)
    }

    def "/chat/removeUser - rpc"() {
        expect:
        1==2
    }

    def "/chat/updates - streaming"() {
        def data = []

        def group = createGroup(rpc)(derek())
        def channelId = group.getPayload(Map).channel_id

        rx.subscribe(new URI("stream://back-end/chat/updates?channel_id=${channelId}&token=${derek().token}"), new Subscriber<StreamData>() {
            @Override
            void onSubscribe(Subscription subscription) { subscription.request(Integer.MAX_VALUE) }

            @Override
            void onNext(StreamData streamData) {
                data << streamData.getPayload(Map)
            }

            @Override
            void onError(Throwable throwable) {

            }

            @Override
            void onComplete() {

            }
        })

        postmessage(channelId)
        postmessage(channelId)
        postmessage(channelId)

        expect:
        new PollingConditions().eventually {
            data.findAll{ it.type == "MessagePosted"}.size() == 3
        }
    }
}
