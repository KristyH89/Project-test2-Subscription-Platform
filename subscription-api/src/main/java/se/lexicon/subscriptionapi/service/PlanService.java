package se.lexicon.subscriptionapi.service;

import se.lexicon.subscriptionapi.domain.enums.ServiceType;
import se.lexicon.subscriptionapi.dto.request.PlanRequest;
import se.lexicon.subscriptionapi.dto.response.PlanResponse;

import java.util.List;

public interface PlanService {

    // ADMIN
    PlanResponse create(PlanRequest request);
    PlanResponse update(Long id, PlanRequest request);
    void delete(Long id);
    List<PlanResponse> findAll(); //

    // ADMIN & CUSTOMER
        PlanResponse findById(Long id);

    // CUSTOMER
    List<PlanResponse> findAllActive();
    List<PlanResponse> findActiveByServiceType(ServiceType serviceType);
    List<PlanResponse> findByOperatorId(Long operatorId);

}
