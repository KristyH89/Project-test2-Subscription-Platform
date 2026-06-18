package se.lexicon.subscriptionapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import se.lexicon.subscriptionapi.domain.enums.ServiceType;

import java.math.BigDecimal;

public record PlanRequest(

        @NotBlank(message = "Plan name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        @Schema(description = "Name of the plan", example = "Fiber 100")
        String name,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.00", inclusive = false, message = "Price must be greater than 0")
        @Digits(integer = 10, fraction = 2, message = "Price can have at most 2 decimal places")
        @Schema(description = "Monthly price of the plan", example = "299.99")
        BigDecimal price,

        @NotNull(message = "Service type is required")
        @Schema(description = "Service type of the plan", example = "INTERNET")
        ServiceType serviceType,

        // null means unlimited data, positive value means limit in MB
        @Positive(message = "Data limit must be a positive number")
        @Schema(description = "Data limit in MB. Null means unlimited",
                example = "5000",
                nullable = true
        )
        Integer dataLimit,

        @NotNull(message = "Operator ID is required")
        @Positive(message = "Operator ID must be a positive number")
        @Schema(description = "ID of the operator offering this plan", example = "2")
        Long operatorId

) {
}
