package newton.cart;

import io.muoncore.newton.AggregateRoot;
import io.muoncore.newton.EventHandler;
import io.muoncore.newton.StreamSubscriptionManager;
import io.muoncore.newton.eventsource.AggregateNotFoundException;
import io.muoncore.newton.eventsource.muon.MuonEventSourceRepository;
import io.muoncore.newton.query.RebuildingDatastoreView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PopularProductsView extends RebuildingDatastoreView {

  private Map<String, Integer> products = new HashMap<>();

  public PopularProductsView(StreamSubscriptionManager streamSubscriptionManager) {
    super(streamSubscriptionManager);
  }

  public Map<String, Integer> getPopularProducts() {
    return Collections.unmodifiableMap(products);
  }

  @Override
  protected Collection<Class<? extends AggregateRoot>> aggregateRoots() {
    return Collections.singletonList(ShoppingCart.class);
  }

  @EventHandler
  public void on(Events.ProductQuantityUpdated ev) {
    int current = products.getOrDefault(ev.getProductId(), 0);
    products.put(ev.getProductId(), current + ev.getQuantityAdded());
  }
}
