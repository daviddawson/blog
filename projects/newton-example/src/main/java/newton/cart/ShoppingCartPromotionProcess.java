package newton.cart;

import io.muoncore.newton.EventHandler;
import io.muoncore.newton.command.CommandFailedEvent;
import io.muoncore.newton.command.CommandIntent;
import io.muoncore.newton.eventsource.AggregateDeletedEvent;
import io.muoncore.newton.saga.SagaStreamConfig;
import io.muoncore.newton.saga.StartSagaWith;
import io.muoncore.newton.saga.StatefulSaga;
import lombok.extern.slf4j.Slf4j;
import newton.CartController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Process to manage upselling around carts
 */
@Scope("prototype")
@Component
@SagaStreamConfig(aggregateRoots = {ShoppingCart.class}, streams = {"requests"})
@Slf4j
public class ShoppingCartPromotionProcess extends StatefulSaga {

  private String cartId;                                                  // <1>

  @StartSagaWith
  public void on(Events.CartCreated ev) {                                 // <2>
    log.info("Cart has been created");
    this.cartId = ev.getId();
    notifyOn(Events.ProductQuantityUpdated.class, "id", cartId);      // <3>
  }

  @EventHandler
  public void on(Events.ProductQuantityUpdated ev) {                      // <4>
    //when a user adds a particular product, add something else to their basket!
    //in real life, you would probably want to not do this.
    if (ev.getProductId().equals("cool-product")) {
      log.info("User added the magic product!  Adding \"new-cool-product\" to their cart for them");
      raiseCommand(CommandIntent.builder(AddProductCommand.class.getCanonicalName())
        .request(new CartController.AddProductRequest(ev.getId(), "new-cool-product", 1000))
        .build());
    }
  }

  @EventHandler
  public void on(AggregateDeletedEvent ev) {
    end();                                                               // <5>
  }

  @EventHandler
  public void on(CommandFailedEvent ev) {                                // <6>
    log.error("Command failed ", ev.getFailureMessage());
  }
}
