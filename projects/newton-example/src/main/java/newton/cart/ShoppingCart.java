package newton.cart;

import io.muoncore.newton.AggregateRoot;
import io.muoncore.newton.EventHandler;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@NoArgsConstructor
public class ShoppingCart extends AggregateRoot<String> {
  private String id;
  private List<CartLine> lines = new ArrayList<>();

  // transactional methods. invoked by commands, generates events
  // can validate and interact with external services, but should mutate no internal state
  public ShoppingCart(String id) {
    raiseEvent(new Events.CartCreated(id));
  }

  public void addProduct(String productId, int quantity, BigDecimal price) {
    raiseEvent(new Events.ProductQuantityUpdated(this.id, productId, price, quantity));
  }

  // handle the event sourcing
  // these should be idempotent, and so shouldn't interact with external services.
  @EventHandler
  public void on(Events.CartCreated ev) {
    this.id = ev.getId();
  }

  @EventHandler
  public void on(Events.ProductQuantityUpdated ev) {
    Optional<CartLine> first = lines.stream().filter(cartLine -> cartLine.getProductId().equals(ev.getProductId())).findFirst();

    CartLine cartLine = first.orElseGet(() -> {
      CartLine line = new CartLine(ev.getQuantityAdded(), ev.getProductId(), ev.getPrice());
      lines.add(line);
      return line;
    });

    cartLine.addQuantity(ev.getQuantityAdded());

    lines.removeIf(line -> line.getQuantity() == 0);
  }
}
