package se.lexicon.subscriptionapi.dto.response;

import se.lexicon.subscriptionapi.domain.enums.ServiceType;

import java.math.BigDecimal;

public record PlanResponse(
        Long id,
        String name,
        BigDecimal price,
        ServiceType serviceType,
        Integer dataLimit,
        boolean active,
        Long operatorId,
        String operatorName
) {}
