package se.lexicon.subscriptionapi.service;

import se.lexicon.subscriptionapi.dto.request.SubscriptionRequest;
import se.lexicon.subscriptionapi.dto.response.SubscriptionResponse;

import java.util.List;

public interface SubscriptionService {

    // CUSTOMER & ADMIN
    SubscriptionResponse create(SubscriptionRequest request);
    SubscriptionResponse findById(Long id);
    List<SubscriptionResponse> findAllByCustomerId(Long customerId);

    // ADMIN
    List<SubscriptionResponse> findAll();
    void delete(Long id);

    // CUSTOMER ACTION
    SubscriptionResponse cancel(Long id);
    SubscriptionResponse changePlan(Long subscriptionId, Long newPlanId);
}
