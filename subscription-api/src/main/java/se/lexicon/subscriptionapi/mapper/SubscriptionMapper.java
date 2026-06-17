package se.lexicon.subscriptionapi.mapper;

import org.springframework.stereotype.Component;
import se.lexicon.subscriptionapi.domain.entity.Subscription;
import se.lexicon.subscriptionapi.dto.request.SubscriptionRequest;
import se.lexicon.subscriptionapi.dto.response.SubscriptionResponse;

@Component
public class SubscriptionMapper {

    /**
     * Maps a Subscription entity to a SubscriptionResponse DTO.
     * Note: customer, plan and operator data are flattened into the response.
     */

    public SubscriptionResponse toResponse(Subscription subscription) {
        if (subscription == null) return null;

        return new SubscriptionResponse(
                subscription.getId(),

                // Customer info
                subscription.getCustomer().getId(),
                subscription.getCustomer().getFirstName() + " " + subscription.getCustomer().getLastName(),

                // Plan info
                subscription.getPlan().getId(),
                subscription.getPlan().getName(),
                subscription.getPlan().getServiceType(),

                // Operator info (via plan)
                subscription.getPlan().getOperator().getId(),
                subscription.getPlan().getOperator().getName(),

                // Subscription status and dates
                subscription.getStatus(),
                subscription.getStartDate(),
                subscription.getCancellationDate(),

                // Audit fields
                subscription.getCreatedAt(),
                subscription.getUpdatedAt()
        );

    }
}


