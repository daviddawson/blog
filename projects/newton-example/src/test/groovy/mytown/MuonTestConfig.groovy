package mytown

import com.google.common.eventbus.EventBus
import io.muoncore.MultiTransportMuon
import io.muoncore.Muon
import io.muoncore.codec.json.JsonOnlyCodecs
import io.muoncore.config.AutoConfiguration
import io.muoncore.config.MuonConfigBuilder
import io.muoncore.eventstore.TestEventStore
import io.muoncore.memory.discovery.InMemDiscovery
import io.muoncore.memory.transport.InMemTransport
import io.muoncore.newton.AggregateEventClient
import io.muoncore.newton.cluster.LockService
import io.muoncore.newton.saga.SagaLoader
import io.muoncore.protocol.event.client.DefaultEventClient
import io.muoncore.protocol.event.client.EventClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("test")
@Configuration
class MuonTestConfig {


    @Autowired
    //Don't remove as it's required for tests - Spring lazy-loads beans & thus causes tests to fail as event store cannot be found!!!
    private TestEventStore testEventStore

    @Bean
    InMemDiscovery discovery() {
        return new InMemDiscovery()
    }

    @Bean
    EventBus bus() {
        return new EventBus()
    }

    @Bean
    Muon muon() {
        AutoConfiguration config = MuonConfigBuilder.withServiceIdentifier("back-end")
                .build()

        return new MultiTransportMuon(config, discovery(),
                Collections.singletonList(
                        new InMemTransport(config, bus())
                ),
                new JsonOnlyCodecs())
    }

    @Bean
    TestEventStore testEventStore() throws InterruptedException {
        AutoConfiguration config = MuonConfigBuilder.withServiceIdentifier("photon-mini")
                .withTags("eventstore")
                .build()
        //Another separate instance of muon is fired up to ensure....
        Muon muon = new MultiTransportMuon(config, discovery(),
                Collections.singletonList(new InMemTransport(config, bus())),
                new JsonOnlyCodecs())

        return new TestEventStore(muon)
    }
    @Bean
    EventClient eventClient(Muon muon) {
        return new DefaultEventClient(muon)
    }

    @Bean
    AggregateEventClient aggregateEventClient(EventClient eventClient) {
        return new AggregateEventClient(eventClient)
    }

    @Bean
    LockService lockService() throws Exception {
        return { name, exec -> exec.execute({}) }
    }

//    @Bean
//    SagaLoader sagaLoader() {
//        return  { interest -> (Class) getClass().class.getClassLoader().loadClass(interest.getSagaClassName()) }
//    }
}
