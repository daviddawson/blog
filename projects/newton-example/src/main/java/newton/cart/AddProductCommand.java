package newton.cart;

import io.muoncore.newton.command.Command;
import io.muoncore.newton.eventsource.EventSourceRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Scope("prototype")
public class AddProductCommand implements Command {

  @Setter
  private String cartId;
  @Setter
  private String productId;
  @Setter
  private int quantity;

  @Autowired
  private EventSourceRepository<ShoppingCart> cartRepository;

  @Override
  public void execute() {
    ShoppingCart cart = cartRepository.load(cartId);
    cart.addProduct(productId, quantity, new BigDecimal("2.5")); //need to look up the actual quantity from some product service!
    cartRepository.save(cart);
  }
}
