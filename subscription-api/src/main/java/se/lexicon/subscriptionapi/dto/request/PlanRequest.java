package se.lexicon.subscriptionapi.dto.request;

import jakarta.validation.constraints.*;
import se.lexicon.subscriptionapi.domain.enums.ServiceType;

import java.math.BigDecimal;

public record PlanRequest(

        @NotBlank(message = "Plan name is required")
        @Size(max = 100, message = "Name cannot exceed 100 characters")
        String name,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.00", inclusive = false, message = "Price must be greater than 0")
        BigDecimal price,

        @NotNull(message = "Service type is required")
        ServiceType serviceType,

        // null means unlimited data, positive value means limit in MB
        @Positive(message = "Data limit must be a positive number")
        Integer dataLimit,

        @NotNull(message = "Operator ID is required")
        Long operatorId

) {
}
