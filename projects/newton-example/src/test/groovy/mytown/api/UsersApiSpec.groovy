package mytown.api

import io.muoncore.Muon
import io.muoncore.protocol.Auth
import io.muoncore.protocol.rpc.Request
import io.muoncore.protocol.rpc.client.RpcClient
import mytown.BackEndApplication
import mytown.MuonTestConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import static mytown.api.Fixtures.derek
import static mytown.api.Fixtures.jenny

@ActiveProfiles("test")
@SpringBootTest(classes = [MuonTestConfig, BackEndApplication])
class UsersApiSpec extends Specification {

    @Autowired
    Muon muon

    RpcClient rpc

    def setup() {
        rpc = new RpcClient(muon)
    }

    def "/user/list"() {

        def create = Fixtures.createUser(rpc)

        create(derek())
        create(jenny())

        def req = new Request(new URI("rpc://back-end/user/list"), [], derek())

        def response = rpc.request(req).get()

        when:
        println "STATUS == ${response.status}"
        def ret = response.getPayload(List)

        then:
        ret.size() == 2

        ret[0].user_id
        ret[0].name == "derek awesome"
        ret[0].picture
        ret[0].online_status
    }
}
