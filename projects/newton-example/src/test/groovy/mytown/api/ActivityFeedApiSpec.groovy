package mytown.api

import io.muoncore.Muon
import io.muoncore.protocol.reactivestream.client.ReactiveStreamClient
import io.muoncore.protocol.rpc.client.RpcClient
import mytown.BackEndApplication
import mytown.MuonTestConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@ActiveProfiles("test")
@SpringBootTest(classes = [MuonTestConfig, BackEndApplication])
class ActivityFeedApiSpec extends Specification {

    @Autowired
    Muon muon

    def "/activity/status - static load of existing feed"() {
        def rpc = new RpcClient(muon)

        when:
        def ret = rpc.request("rpc://back-end/activity/status", [user:"Hanna"]).get()

        def pay = ret.getPayload(Map)
        then:
        ret.status == 200
        pay.feed[0].text
    }

    def "/activity/myfeed - stream push of new items"() {
        def stream = new ReactiveStreamClient(muon)

        when:

        expect:
        1==2
    }

    def "/activity/post - add item to feed"() {
        expect:
        1==2
    }
}
