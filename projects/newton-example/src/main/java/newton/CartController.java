package newton;

import io.muoncore.newton.command.CommandBus;
import io.muoncore.newton.command.CommandIntent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import newton.cart.AddProductCommand;
import newton.cart.CartService;
import newton.cart.PopularProductsView;
import newton.cart.ShoppingCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class CartController {

  @Autowired
  private CommandBus bus;

  @Autowired
  private CartService cart;
  @Autowired
  private PopularProductsView popularProductsView;

  @RequestMapping(value = "/{cartId}", method = RequestMethod.GET)
  public ShoppingCart getCart(@PathVariable("cartId") String cartId) {
    return cart.getCart(cartId);
  }

  @RequestMapping(value = "/{cartId}/add/{productId}", method = RequestMethod.GET)
  public ShoppingCart addProduct(AddProductRequest request) {
    bus.dispatch(CommandIntent.builder(AddProductCommand.class.getCanonicalName())
      .request(request)
      .build());
    return cart.getCart(request.cartId);
  }

  @RequestMapping(value = "/popular", method = RequestMethod.GET)
  public Map<String, Integer> getPopularProducts() {
    return popularProductsView.getPopularProducts();
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Data
  public static class AddProductRequest {
    private String cartId;
    private String productId;
    private int quantity;
  }
}
