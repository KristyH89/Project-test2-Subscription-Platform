package se.lexicon.subscriptionapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.lexicon.subscriptionapi.domain.entity.Plan;
import se.lexicon.subscriptionapi.domain.enums.ServiceType;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    // All active plans for CUSTOMER
    List<Plan> findByActiveTrue();

    // Active plans per service type for CUSTOMER
    List<Plan> findByActiveTrueAndServiceType(ServiceType serviceType);

    // All plans from an operator for CUSTOMER
    List<Plan> findByOperatorId(Long operatorId);

    // Prevents duplicate plan names for the same operator
    boolean existsByNameAndOperatorId(String name, Long operatorId);
}
