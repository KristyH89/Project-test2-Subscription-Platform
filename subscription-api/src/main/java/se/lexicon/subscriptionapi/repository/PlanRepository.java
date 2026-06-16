package se.lexicon.subscriptionapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.lexicon.subscriptionapi.domain.entity.Plan;

public interface PlanRepository extends JpaRepository<Plan, Long> {
}
