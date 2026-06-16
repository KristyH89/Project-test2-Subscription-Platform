package se.lexicon.subscriptionapi.mapper;

import org.mapstruct.*;
import se.lexicon.subscriptionapi.domain.entity.Operator;
import se.lexicon.subscriptionapi.domain.entity.Plan;
import se.lexicon.subscriptionapi.dto.request.PlanRequest;
import se.lexicon.subscriptionapi.dto.response.PlanResponse;

@Mapper(componentModel = "spring")
public interface PlanMapper {

    /* -------------------------------------------------------------
        PlanRequest -> Plan (create)
       ------------------------------------------------------------- */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "operator", source = "operatorId", qualifiedByName = "mapOperator")
    @Mapping(target = "active", constant = "true") // default active
    Plan toEntity(PlanRequest request);

    /* -------------------------------------------------------------
        Plan -> PlanResponse
       ------------------------------------------------------------- */
    @Mapping(target = "operatorId", source = "operator.id")
    @Mapping(target = "operatorName", source = "operator.name")
    PlanResponse toResponse(Plan plan);

    /* -------------------------------------------------------------
        Helper: operatorID -> Operator
       ------------------------------------------------------------- */
    @Named("mapOperator")
    default Operator mapOperator(Long operatorId) {
        if (operatorId == null) return null;
        Operator operator = new Operator();
        operator.setId(operatorId);
        return operator;
    }
}
