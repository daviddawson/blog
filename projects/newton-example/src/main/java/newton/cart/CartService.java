package newton.cart;

import io.muoncore.newton.eventsource.AggregateNotFoundException;
import io.muoncore.newton.eventsource.muon.MuonEventSourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartService {

  @Autowired
  private MuonEventSourceRepository<ShoppingCart> repo;

  public ShoppingCart getCart(String id) {
    try {
      return repo.load(id);
    } catch (AggregateNotFoundException e) {
      return repo.newInstance(() -> new ShoppingCart(id));
    }
  }
}
