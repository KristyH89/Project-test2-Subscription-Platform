package se.lexicon.subscriptionapi.dto.request;

import jakarta.validation.constraints.NotNull;

public record SubscriptionRequest(

        @NotNull(message = "Customer ID is required")
        Long customerId,

        @NotNull(message = "Plan ID is required")
        Long planId

) {}
