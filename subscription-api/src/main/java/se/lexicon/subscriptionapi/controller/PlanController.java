package se.lexicon.subscriptionapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.lexicon.subscriptionapi.domain.enums.ServiceType;
import se.lexicon.subscriptionapi.dto.request.PlanRequest;
import se.lexicon.subscriptionapi.dto.response.PlanResponse;
import se.lexicon.subscriptionapi.service.PlanService;

import java.util.List;

@Tag(name = "Plans", description = "Plan management endpoints.")
@RestController
@RequestMapping("/api/v1/plans")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PlanController {

    private final PlanService planService;

    // ----------------------------------------
    // ADMIN
    // ----------------------------------------

    /**
     * Create a new plan for an operator.
     *
     * @param request the plan creation request
     * @return the created plan
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a plan", description = "Requires JWT.\n\nRoles: ADMIN")
    public PlanResponse create(@Valid @RequestBody PlanRequest request) {
        return planService.create(request);
    }

    /**
     * Update an existing plan.
     *
     * @param id      the ID of the plan to update
     * @param request the updated plan data
     * @return the updated plan
     */

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a plan", description = "Requires JWT.\n\nRoles: ADMIN")
    public PlanResponse update(@PathVariable Long id, @Valid @RequestBody PlanRequest request) {
        return planService.update(id, request);
    }

    /**
     * Delete a plan by ID.
     *
     * @param id the ID of the plan to delete
     */

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a plan", description = "Requires JWT. \n\nRoles: ADMIN")
    public void delete(@PathVariable Long id) {
        planService.delete(id);
    }

    /**
     * Get all plans (active + inactive)
     * Admin only.
     *
     * @return list of all plans
     */

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all plans", description = "Returns active and inactive plans.\n\nRoles: ADMIN")
    public List<PlanResponse> findAll() {
        return planService.findAll();
    }

    // ----------------------------------------
    // ADMIN & CUSTOMER
    // ----------------------------------------

    /**
     * Get a plan by ID
     *
     * @param id the plan ID
     * @return the plan
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get plan by ID", description = "Requires JWT. \n\nRoles: USER, ADMIN")
    public PlanResponse findById(@PathVariable Long id) {
        return planService.findById(id);
    }

    // ----------------------------------------
    // CUSTOMER
    // ----------------------------------------

    /**
     * Returns all active plans.
     *
     * @return list of active plans
     */

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get all active plans", description = "Requires JWT. \n\nRoles: USER, ADMIN")
    public List<PlanResponse> findAllActive() {
        return planService.findAllActive();
    }

    /**
     * Get all active plans filtered by service type.
     *
     * @param serviceType the service type (MOBILE or INTERNET)
     * @return list of active plans for the given type
     */

    @GetMapping("/active/type")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get active plans by service type", description = "Requires JWT. \n\nRoles: USER, ADMIN")
    public List<PlanResponse> findActiveByServiceType(@RequestParam ServiceType serviceType) {
        return planService.findActiveByServiceType(serviceType);
    }

    /**
     * Get all plans belonging to a specific operator.
     *
     * @param operatorId the operator ID
     * @return list of plans for the operator
     */

    @GetMapping("/operator/{operatorId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get plans by operator", description = "Requires JWT. \n\nRoles: USER, ADMIN")
    public List<PlanResponse> findByOperatorId(@PathVariable Long operatorId) {
        return planService.findByOperatorId(operatorId);
    }
}