package newton.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * Represent a product in the cart
 */
@Getter
@AllArgsConstructor
public class CartLine {
  private int quantity;
  private String productId;
  private BigDecimal price;

  public BigDecimal getSubtotal() {
    return price.multiply(new BigDecimal(quantity));
  }

  public void addQuantity(int quantity) {
    this.quantity += quantity;
  }
}
