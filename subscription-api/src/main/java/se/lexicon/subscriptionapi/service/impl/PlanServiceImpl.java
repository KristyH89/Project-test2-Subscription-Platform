package se.lexicon.subscriptionapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.subscriptionapi.domain.entity.Operator;
import se.lexicon.subscriptionapi.domain.entity.Plan;
import se.lexicon.subscriptionapi.domain.enums.ServiceType;
import se.lexicon.subscriptionapi.dto.request.PlanRequest;
import se.lexicon.subscriptionapi.dto.response.PlanResponse;
import se.lexicon.subscriptionapi.exception.BusinessRuleException;
import se.lexicon.subscriptionapi.exception.ResourceNotFoundException;
import se.lexicon.subscriptionapi.mapper.PlanMapper;
import se.lexicon.subscriptionapi.repository.OperatorRepository;
import se.lexicon.subscriptionapi.repository.PlanRepository;
import se.lexicon.subscriptionapi.service.PlanService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final OperatorRepository operatorRepository;
    private final PlanMapper planMapper;

    // ADMIN

    @Override
    public PlanResponse create (PlanRequest request) {

        // Check if the operator exists
        Operator operator = operatorRepository.findById(request.operatorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Operator not found with id: " + request.operatorId()));

        // Prevent duplicate plan names for the same operator
        if (planRepository.existsByNameAndOperatorId(request.name(), request.operatorId()) {
            throw new BusinessRuleException(
                    "Plan with name '" + request.name() + "' already exists for this operator");
        }
        // The mapper creates the Plan, we assign the operator ourselves
        // (the mapper creates an empty Operator with only the id, but we want the actual one)

        Plan plan = planMapper.toEntity(request);
        plan.setOperator(operator);

        return planMapper.toResponse(planRepository.save(plan));
    }

    @Override
    public PlanResponse update(Long id, PlanRequest request) {

        // Retrieve the existing plan
        Plan existing = planRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Plan not found with id: " + id));

        // Check if the new operator exists
        Operator operator = operatorRepository.findById(request.operatorId())
                 .orElseThrow(() -> new ResourceNotFoundException(
                         "Operator not found with id: " + request.operatorId()));

        // Prevent duplicate plan names for the same operator (but excludes the current one)
        boolean nameConflict= planRepository.existsByNameAndOperatorId(
                request.name(), request.operatorId());
        boolean isSamePlan = existing.getName().equals(request.name())
                && existing.getOperator().getId().equals(request.operatorId());

        if (nameConflict && !isSamePlan) {
            throw new BusinessRuleException(
                    "Plan with name '" + request.name() + "' already exists for this operator");
        }
        // update fields
                existing.setName(request.name());
                existing.setPrice(request.price());
                existing.setServiceType(request.serviceType());
                existing.setDataLimit(request.dataLimit());
                existing.setOperator(operator);

                return planMapper.toResponse(planRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        if (!planRepository.existsById(id)) {
            throw new ResourceNotFoundException("Plan not found");
        }
        planRepository.deleteById(id);
    }

    // ADMIN & CUSTOMER

    @Override
    @Transactional(readOnly = true)
    public PlanResponse findById(Long id) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Plan not found with id: " + id));
        return planMapper.toResponse(plan);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanResponse> findAll(){
        // Returns all plans, including inactive ones, ADMIN only
        return planRepository.findAll()
                .stream()
                .map(planMapper::toResponse)
                .toList();
    }

    // CUSTOMER

    @Override
    @Transactional(readOnly = true)
    public List<PlanResponse> findAllActive() {

        // The database filters directly on active = true, more efficient than retrieving everything
        return planRepository.findByActiveTrue()
                .stream()
                .map(planMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanResponse> findActiveByServiceType(ServiceType serviceType) {

        // The database filters on active = true and the correct service type
        return planRepository.findByActiveTrueAndServiceType(serviceType)
                .stream()
                .map(planMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanResponse> findByOperatorId(Long operatorId) {

        // First check if the operator exists
        if (!operatorRepository.existsById(operatorId)) {
            throw new ResourceNotFoundException(
                    "Operator not found with id: " + operatorId);
        }

        return planRepository.findByOperatorId(operatorId)
                .stream()
                .map(planMapper::toResponse)
                .toList();
    }
}
