package se.lexicon.subscriptionapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.lexicon.subscriptionapi.domain.entity.Subscription;
import se.lexicon.subscriptionapi.domain.enums.ServiceType;
import se.lexicon.subscriptionapi.domain.enums.SubscriptionStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    // All subscriptions for a customer
    List<Subscription> findAllByCustomerId(Long customerId);

    // Checks whether a customer-plan combination already exists
    Optional<Subscription> findByCustomerIdAndPlanId(Long customerId, Long planId);

    // Core business rule: a customer can have at most one active subscription per service type
    Optional<Subscription> findByCustomerIdAndPlan_ServiceTypeAndStatus(
            Long customerId, ServiceType serviceType, SubscriptionStatus status);

}
