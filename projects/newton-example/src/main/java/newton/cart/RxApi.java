package newton.cart;

import io.muoncore.newton.eventsource.EventSourceRepository;
import io.muoncore.protocol.reactivestream.server.PublisherLookup;
import io.muoncore.protocol.reactivestream.server.ReactiveStreamServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class RxApi {

  @Autowired
  private ReactiveStreamServer rxServer;

  @Autowired
  private EventSourceRepository<ShoppingCart> repo;

  @PostConstruct
  public void after() {
    rxServer.publishGeneratedSource("basket", PublisherLookup.PublisherType.HOT, reactiveStreamSubscriptionRequest -> {
      log.info("Subscribing to cart updates for {}", reactiveStreamSubscriptionRequest.getArgs());
      return repo.susbcribeAggregateUpdates(reactiveStreamSubscriptionRequest.getArgs().get("id"));
    });
  }
}
