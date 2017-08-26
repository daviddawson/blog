package newton.cart;

import io.muoncore.newton.NewtonEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

public class Events {

  @Getter
  @AllArgsConstructor
  public static class CartCreated implements NewtonEvent<String> {
    private String id;
  }

  @Getter
  @AllArgsConstructor
  public static class ProductQuantityUpdated implements NewtonEvent<String> {
    private String id;
    private String productId;
    private BigDecimal price;
    private int quantityAdded;
  }

}
