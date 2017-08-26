package mytown.api

import io.muoncore.Muon
import io.muoncore.eventstore.TestEventStore
import io.muoncore.newton.eventsource.EventSourceRepository
import io.muoncore.protocol.reactivestream.client.ReactiveStreamClient
import io.muoncore.protocol.reactivestream.client.StreamData
import io.muoncore.protocol.rpc.Request
import io.muoncore.protocol.rpc.client.RpcClient
import mytown.BackEndApplication
import mytown.MuonTestConfig
import mytown.basket.Basket
import mytown.product.ProductCreated
import mytown.product.ProductQueryService
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Timeout
import spock.util.concurrent.PollingConditions

import static mytown.api.Fixtures.derek
import static mytown.api.Fixtures.jenny
import static mytown.api.Fixtures.user

@ActiveProfiles("test")
@SpringBootTest(classes = [MuonTestConfig, BackEndApplication])
class BasketApiSpec extends Specification {


    @Autowired
    TestEventStore eventStore

    @Autowired
    Muon muon

    @Autowired
    ProductQueryService productQueryService

    @Autowired
    EventSourceRepository<Basket> basketRepo

    RpcClient rpc
    ReactiveStreamClient rx

    def setup() {
        rpc = new RpcClient(muon)
        rx = new ReactiveStreamClient(muon)
        def create = Fixtures.createUser(rpc)

        create(derek())
        create(jenny())
    }

    def "/basket/add - rpc"() {

        given: "a product in the db"
        productQueryService.on(new ProductCreated(productId: "123", offerPriceDetails: [amount: 15]))

        when: "user adds to the basket"
        def req = rpc.request(new Request(new URI("rpc://back-end/basket/add"), [productId: "123"], derek())).get()

        println "RET = ${req.getPayload(Map)}"

        req = rpc.request(new Request(new URI("rpc://back-end/basket"), [], derek())).get()

        println "STATUS IS ${req.status}"

        def ret = req.getPayload(Map)

        then:
        ret.items.size() == 1
        ret.subtotal == 15
    }

    def "/basket/setquantity - rpc"() {
        given: "a product in the db"
        productQueryService.on(new ProductCreated(productId: "123", offerPriceDetails: [amount: 15]))

        when: "user adds to the basket"
        def req = rpc.request(new Request(new URI("rpc://back-end/basket/setquantity"), [productId: "123", amount:10], derek())).get()

        println "RET = ${req.getPayload(Map)}"

        req = rpc.request(new Request(new URI("rpc://back-end/basket"), [], derek())).get()

        println "STATUS IS ${req.status}"

        def ret = req.getPayload(Map)

        then:
        ret.items.size() == 1
        ret.subtotal == 150
    }

//    @Timeout(10)
    def "/basket/state - streaming"() {

        Subscription sub
        def data = []

        rx.subscribe(new URI("stream://back-end/basket?token=${derek().token}"), new Subscriber<StreamData>() {
            @Override
            void onSubscribe(Subscription subscription) {
                println "GOT SUB"
                sub = subscription
                subscription.request(Integer.MAX_VALUE) }

            @Override
            void onNext(StreamData streamData) {
                println "GOT BASEK!T"
                data << streamData.getPayload(Map)
            }

            @Override
            void onError(Throwable throwable) {
                println "ERRORREDDDD"
                throwable.printStackTrace()
            }

            @Override
            void onComplete() {
                println "COMPLETED SUBSCRIPTION"
            }
        })

        sleep 200
        when: "user adds to the basket"
        println "ADDING ITEM TO BASEKT"
        def basket = basketRepo.load(user(derek()).id)
        basket.addProduct(productQueryService.getProduct("123"))
        basketRepo.save(basket)

        then:
        new PollingConditions(timeout: 8).eventually {
            data.size() > 2
        }

//        cleanup:
//        sub.cancel()
    }
}
