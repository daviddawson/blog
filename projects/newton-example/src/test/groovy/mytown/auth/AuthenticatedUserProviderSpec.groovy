package mytown.auth

import io.muoncore.newton.StreamSubscriptionManager
import io.muoncore.newton.eventsource.EventSourceRepository
import io.muoncore.protocol.Auth
import spock.lang.Specification

class AuthenticatedUserProviderSpec extends Specification {

    def "creates a new user when non exists for a google token"() {
        def provider = new AuthenticatedUserProvider()
        def auth = new Auth(provider:"google", token: "")

        when:
        def ret = provider.getUser(auth)

        then:
        ret
        ret.first == "david"
        ret.last == "dawson"

    }

    def "returns an existing auth user for an existing mytown token"() {

        def stream = Mock(StreamSubscriptionManager)

        def user = new AuthenticatedUser(id: "random", email: "d@d.com", first: "david", last: "dawson", permissions: [])

        def provider = new AuthenticatedUserProvider(stream)
        provider.feedRepo = Mock(EventSourceRepository)
        provider.userRepo = Mock(EventSourceRepository)

        def auth = new Auth(provider:"myt", token: user.jwt)

        when:
        def ret = provider.getUser(auth)

        then:
        ret
        ret.first == "david"
        ret.last == "dawson"

    }


}
