package UMC.career_mate.domain.planner.dto.request;

import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Builder
public record CreatePlannerDTO(

        @Length(max=200, message = "최대 200자까지 입력 가능합니다.")
        String activityName,
        LocalDateTime startTime,
        LocalDateTime endTime,
        @Length(max=1000, message = "최대 1000자까지 입력 가능합니다.")
        String specific,
        @Length(max=1000, message = "최대 1000자까지 입력 가능합니다.")
        String measurable,
        @Length(max=1000, message = "최대 1000자까지 입력 가능합니다.")
        String achievable,
        @Length(max=1000, message = "최대 1000자까지 입력 가능합니다.")
        String relevant,
        @Length(max=1000, message = "최대 1000자까지 입력 가능합니다.")
        String timeBound,
        @Length(max=1000, message = "최대 1000자까지 입력 가능합니다.")
        String otherPlans
) {
}
