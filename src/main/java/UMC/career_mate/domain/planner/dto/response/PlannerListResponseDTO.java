package UMC.career_mate.domain.planner.dto.response;

import UMC.career_mate.domain.planner.Planner;
import lombok.Builder;

import java.util.List;

@Builder
public record PlannerListResponseDTO(
        List<PlannerResponseDTO> planners
) {
}
