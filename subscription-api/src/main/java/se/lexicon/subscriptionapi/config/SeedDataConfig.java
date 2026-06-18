package se.lexicon.subscriptionapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import se.lexicon.subscriptionapi.domain.entity.Operator;
import se.lexicon.subscriptionapi.domain.entity.Plan;
import se.lexicon.subscriptionapi.domain.enums.ServiceType;
import se.lexicon.subscriptionapi.repository.OperatorRepository;
import se.lexicon.subscriptionapi.repository.PlanRepository;

import java.math.BigDecimal;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SeedDataConfig implements CommandLineRunner {

    private final OperatorRepository operatorRepository;
    private final PlanRepository planRepository;

    @Override
    public void run(String... args) {

        // Seed plans only if none exist yet
        if (operatorRepository.count() > 0 && planRepository.count() > 0) {
            return;
        }

    // --------------------------------
    // OPERATORS
    // --------------------------------
    Operator fiberNet = operatorRepository.findByName("FiberNet")
             .orElseGet(() ->
                    operatorRepository.save(
                            Operator.builder()
                                    .name("FiberNet")
                                    .build()
                            )
                    );

   Operator mobilePlus = operatorRepository.findByName("MobilePlus")
            .orElseGet(() ->
                   operatorRepository.save(
                           Operator.builder()
                                   .name("MobilePlus")
                                   .build()
                           )
                   );


   // --------------------------------
   // FIBERNET PLANS
   // --------------------------------
        List<Plan> fiberPlans = List.of(
                Plan.builder()
                        .name("Fiber 50")
                        .price(new BigDecimal("299.99"))
                        .serviceType(ServiceType.INTERNET)
                        .dataLimit(null)
                        .active(true)
                        .operator(fiberNet)
                        .build(),

                Plan.builder()
                        .name("Fiber 100")
                        .price(new BigDecimal("399.99"))
                        .serviceType(ServiceType.INTERNET)
                        .dataLimit(null)
                        .active(true)
                        .operator(fiberNet)
                        .build(),

                Plan.builder()
                        .name("Fiber 300")
                        .price(new BigDecimal("599.99"))
                        .serviceType(ServiceType.INTERNET)
                        .dataLimit(null)
                        .active(false)
                        .operator(fiberNet)
                        .build()
        );


   // --------------------------------
   // MOBILEPLUS PLANS
   // --------------------------------

        List<Plan> mobilePlans = List.of(
                Plan.builder()
                        .name("Mobile Basic")
                        .price(new BigDecimal("149.99"))
                        .serviceType(ServiceType.MOBILE)
                        .dataLimit(5000)
                        .active(true)
                        .operator(mobilePlus)
                        .build(),

                Plan.builder()
                        .name("Mobile Plus")
                        .price(new BigDecimal("249.99"))
                        .serviceType(ServiceType.MOBILE)
                        .dataLimit(15000)
                        .active(true)
                        .operator(mobilePlus)
                        .build(),

                Plan.builder()
                        .name("Mobile Unlimited")
                        .price(new BigDecimal("349.99"))
                        .serviceType(ServiceType.MOBILE)
                        .dataLimit(null)
                        .active(false)
                        .operator(mobilePlus)
                        .build()
        );

        planRepository.saveAll(fiberPlans);
        planRepository.saveAll(mobilePlans);



            System.out.println(" \uD83C\uDF31 Seed data loaded successfully!");

        }
    }

