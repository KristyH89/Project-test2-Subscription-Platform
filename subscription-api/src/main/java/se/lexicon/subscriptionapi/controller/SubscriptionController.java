package se.lexicon.subscriptionapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.lexicon.subscriptionapi.dto.request.SubscriptionRequest;
import se.lexicon.subscriptionapi.dto.response.SubscriptionResponse;
import se.lexicon.subscriptionapi.service.SubscriptionService;

import java.util.List;

@Tag(name = "Subscriptions", description = "Subscription management endpoints.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscriptions")
@SecurityRequirement(name = "bearerAuth")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    // ----------------------------------------
    // CUSTOMER & ADMIN
    // ----------------------------------------

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Create subscription", description = "Requires JWT.\n\nRoles: USER, ADMIN")
    public ResponseEntity<SubscriptionResponse> create(@Valid @RequestBody SubscriptionRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(subscriptionService.create(request));

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get subscription by ID", description = "Requires JWT.\n\nRoles: USER, ADMIN")
    public ResponseEntity<SubscriptionResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(subscriptionService.findById(id));
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get subscription by customer", description = "Requires JWT.\n\nRoles: USER, ADMIN")

    public ResponseEntity<List<SubscriptionResponse>> findAllByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(subscriptionService.findAllByCustomerId(customerId));
    }

    // ----------------------------------------
    // ADMIN
    // ----------------------------------------

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all subscriptions", description = "Requires JWT.\n\nRoles: ADMIN")
    public ResponseEntity<List<SubscriptionResponse>> findAll() {
        return ResponseEntity.ok(subscriptionService.findAll());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete subscription", description = "Requires JWT.\n\nRoles: ADMIN")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subscriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ----------------------------------------
    // CUSTOMER ACTIONS
    // ----------------------------------------

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Cancel subscription", description = "Requires JWT.\n\nRoles: USER, ADMIN")
    public ResponseEntity<SubscriptionResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(subscriptionService.cancel(id));
    }


    @PutMapping("/{id}/change-plan/{newPlanId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Change subscription plan", description = "Plan change allowed only within same operator and service type.\n\nRoles: USER, ADMIN")
    public ResponseEntity<SubscriptionResponse> changePlan(
            @PathVariable Long id,
            @PathVariable Long newPlanId
    ) {
        return ResponseEntity.ok(subscriptionService.changePlan(id, newPlanId));
    }
}
