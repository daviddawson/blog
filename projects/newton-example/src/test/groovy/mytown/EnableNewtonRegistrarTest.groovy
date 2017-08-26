package mytown;

import io.muoncore.newton.EnableNewton;
import io.muoncore.newton.eventsource.EventSourceRepository;
import mytown.users.Feed;
import mytown.users.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@ActiveProfiles(["test", "log-events"])
@Import([MuonTestConfig.class])
@RunWith(SpringRunner.class)
@Configuration
@EnableNewton
public class EnableNewtonRegistrarTest {

  @Autowired
  EventSourceRepository<User> repo;

  @Test
  public void testAutoCreatedRepo() {
    assertNotNull(repo);
    User user = new User("Dvid", "Dawson", new Feed());

    repo.newInstance({ user });

    assertNotNull(repo.load(user.getId()));
  }
}
