package newton;

import io.muoncore.Muon;
import io.muoncore.newton.EnableNewton;
import io.muoncore.protocol.reactivestream.server.ReactiveStreamServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableNewton
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class);
  }

  @Bean
  public ReactiveStreamServer rxServer(Muon muon) {
    return new ReactiveStreamServer(muon);
  }
}
