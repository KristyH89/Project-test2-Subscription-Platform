package se.lexicon.subscriptionapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.subscriptionapi.domain.entity.Customer;
import se.lexicon.subscriptionapi.domain.entity.Plan;
import se.lexicon.subscriptionapi.domain.entity.Subscription;
import se.lexicon.subscriptionapi.domain.enums.SubscriptionStatus;
import se.lexicon.subscriptionapi.dto.request.SubscriptionRequest;
import se.lexicon.subscriptionapi.dto.response.SubscriptionResponse;
import se.lexicon.subscriptionapi.exception.BusinessRuleException;
import se.lexicon.subscriptionapi.exception.ResourceNotFoundException;
import se.lexicon.subscriptionapi.mapper.SubscriptionMapper;
import se.lexicon.subscriptionapi.repository.CustomerRepository;
import se.lexicon.subscriptionapi.repository.PlanRepository;
import se.lexicon.subscriptionapi.repository.SubscriptionRepository;
import se.lexicon.subscriptionapi.service.SubscriptionService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final CustomerRepository customerRepository;
    private final PlanRepository planRepository;
    private final SubscriptionMapper subscriptionMapper;

    // -------------------------------------
    // CREATE SUBSCRIPTION
    // -------------------------------------
    @Override
    public SubscriptionResponse create(SubscriptionRequest request) {

        // Validate customer
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        // Validate plan
        Plan plan = planRepository.findById(request.planId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found"));

        // Business rule: customer cannot subscribe twice to the same plan
        subscriptionRepository.findByCustomerIdAndPlanId(customer.getId(), plan.getId())
                .ifPresent(s -> {
                    throw new BusinessRuleException("Customer already has a subscription for this plan");

                });

        // Business rule: customer may have only one ACTIVE subscription per service type
        subscriptionRepository.findByCustomerIdAndPlan_ServiceTypeAndStatus(
                customer.getId(),
                plan.getServiceType(),
                SubscriptionStatus.ACTIVE
        ).ifPresent(s -> {
        throw new BusinessRuleException("Customer already has an active subscription for this service type");
        });

        // Create new subscription
        Subscription subscription = Subscription.builder()
                .customer(customer)
                .plan(plan)
                .status(SubscriptionStatus.ACTIVE)
                .startDate(LocalDate.now())
                .cancellationDate(null)
                .build();

        return subscriptionMapper.toResponse(subscriptionRepository.save(subscription));
}
    // -------------------------------------
    // FIND BY ID
    // -------------------------------------
    @Override
    @Transactional(readOnly = true)
    public SubscriptionResponse findById(Long id) {
    Subscription subscription = subscriptionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));

    return subscriptionMapper.toResponse(subscription);
    }

    // -------------------------------------
    // FIND ALL BY CUSTOMER
    // -------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionResponse> findAllByCustomerId(Long customerId) {
        return subscriptionRepository.findAllByCustomerId(customerId)
                .stream()
                .map(subscriptionMapper::toResponse)
                .toList();
    }

    // -------------------------------------
    // ADMIN: FIND ALL SUBSCRIPTIONS
    // -------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionResponse> findAll() {
        return subscriptionRepository.findAll()
                .stream()
                .map(subscriptionMapper::toResponse)
                .toList();
    }

    // -------------------------------------
    // ADMIN: DELETE SUBSCRIPTION
    // -------------------------------------
    @Override
    public void delete(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));

        subscriptionRepository.delete(subscription);
    }

    // -------------------------------------
    // CUSTOMER: CANCEL SUBSCRIPTION
    // -------------------------------------
    @Override
    public SubscriptionResponse cancel(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));

        // Business rule: cannot cancel an already cancelled subscription
        if (subscription.getStatus() == SubscriptionStatus.CANCELLED) {
            throw new BusinessRuleException("Subscription is already cancelled");
        }

        subscription.setStatus(SubscriptionStatus.CANCELLED);
        subscription.setCancellationDate(LocalDate.now());

        return subscriptionMapper.toResponse(subscriptionRepository.save(subscription));
    }

    // -------------------------------------
    // CUSTOMER: CHANGE PLAN
    // -------------------------------------
    @Override
    public SubscriptionResponse changePlan(Long subscriptionId, Long newPlanId) {

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));

        Plan newPlan = planRepository.findById(newPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("New plan not found"));

        // Business rule: only active subscriptions can change plan
        if (subscription.getStatus() != SubscriptionStatus.ACTIVE) {
            throw new BusinessRuleException("Only active subscriptions can change plan");
        }

        // Business rule: cannot switch to the same plan
        if (subscription.getPlan().getId().equals(newPlanId)) {
            throw new BusinessRuleException("Subscription is already using this plan");
        }

        // Business rule: prevent duplicate subscription for the new plan
        subscriptionRepository.findByCustomerIdAndPlanId(subscription.getCustomer().getId(), newPlanId)
                .ifPresent(s -> {
                    throw new BusinessRuleException("Customer already has a subscription for this plan");
                });

        // Business rule: customer may have only one ACTIVE subscription per service type
        subscriptionRepository.findByCustomerIdAndPlan_ServiceTypeAndStatus(
                subscription.getCustomer().getId(),
                newPlan.getServiceType(),
                SubscriptionStatus.ACTIVE
        ).ifPresent(s -> {
            if (!s.getId().equals(subscriptionId)) {
                throw new BusinessRuleException("Customer already has an active subscription");
            }
        });

        // Business rule: new plan must be from the same operator
        if (!newPlan.getOperator().getId().equals(subscription.getPlan().getOperator().getId())) {
            throw new BusinessRuleException("New plan must be from the same operator");
        }

        // Business rule: new plan must be the same service type
        if (newPlan.getServiceType() != subscription.getPlan().getServiceType()) {
            throw new BusinessRuleException("New plan must be the same service type");
        }

        // Apply plan change
        subscription.setPlan(newPlan);

        return subscriptionMapper.toResponse(subscriptionRepository.save(subscription));

    }


}

