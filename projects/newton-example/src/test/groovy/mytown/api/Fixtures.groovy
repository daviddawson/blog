package mytown.api

import io.muoncore.protocol.Auth
import io.muoncore.protocol.rpc.Request
import io.muoncore.protocol.rpc.client.RpcClient
import mytown.auth.AuthenticatedUser

class Fixtures {

    static def createUser(RpcClient rpc) {
        return { auth ->
            def req = new Request(new URI("rpc://back-end/user/login"), [], auth)
            def response = rpc.request(req).get()
        }
    }

    static def createGroup(RpcClient rpc) {
        return { auth ->
            def req = new Request(new URI("rpc://back-end/chat/create"), [], auth)
            rpc.request(req).get()
        }
    }

    static Auth derek() {
        def user = new AuthenticatedUser(id: "derek", email: "d@d.com", first: "derek", last: "awesome", permissions: [])
        new Auth("myt", user.jwt)
    }

    static Auth jenny() {
        def user = new AuthenticatedUser(id: "random2", email: "d@d2.com", first: "jenny", last: "awesome", permissions: [])
        new Auth("myt", user.jwt)
    }

    static AuthenticatedUser user(Auth auth) {
        AuthenticatedUser.fromJWT(auth.token)
    }



}
