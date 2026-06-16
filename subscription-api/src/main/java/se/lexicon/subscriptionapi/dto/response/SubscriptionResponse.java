package se.lexicon.subscriptionapi.dto.response;

import se.lexicon.subscriptionapi.domain.enums.ServiceType;
import se.lexicon.subscriptionapi.domain.enums.SubscriptionStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SubscriptionResponse(

        Long id,

        Long customerId,
        String customerName,

        Long planId,
        String planName,
        ServiceType serviceType,

        Long operatorId,
        String operatorName,

        SubscriptionStatus status,

        LocalDate startDate,
        LocalDate cancellationDate,

        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {}
