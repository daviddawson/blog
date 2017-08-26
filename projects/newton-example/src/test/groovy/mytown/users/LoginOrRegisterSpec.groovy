package mytown.users

import io.muoncore.newton.EnableNewton
import io.muoncore.newton.command.CommandBus
import io.muoncore.newton.eventsource.EventSourceRepository
import mytown.BackEndApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

@ActiveProfiles("test")
@SpringBootTest(classes = [BackEndApplication])
@EnableNewton
class LoginOrRegisterSpec extends Specification {

    @Autowired
    EventSourceRepository<User> repository

    @Autowired
    UserListService view
    @Autowired
    CommandBus bus

    def "Start"() {


        when:
        1.toString()

        then:
        new PollingConditions(timeout: 5).eventually {
            view.user {
                it.contains("David")
            }
        }
    }

    def "On"() {
    }

    def "On1"() {
    }
}
