package se.lexicon.subscriptionapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SubscriptionRequest(

        @NotNull(message = "Customer ID is required")
        @Positive(message = "Customer ID must be a positive number")
        @Schema(description = "ID of the customer creating the subscription", example = "1")
        Long customerId,

        @NotNull(message = "Plan ID is required")
        @Positive(message = "Plan ID must be a positive number")
        @Schema(description = "ID of the plan to subscribe to", example = "5")
        Long planId
) {}
