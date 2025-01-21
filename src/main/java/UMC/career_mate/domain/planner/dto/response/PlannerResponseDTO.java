package UMC.career_mate.domain.planner.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PlannerResponseDTO(
        String activityName,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String specifics,
        String measurable,
        String achievable,
        String relevant,
        String timeBound,
        String otherPlans
) {
}
