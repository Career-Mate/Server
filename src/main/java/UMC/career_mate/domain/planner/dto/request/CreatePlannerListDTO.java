package UMC.career_mate.domain.planner.dto.request;

import UMC.career_mate.global.response.exception.GeneralException;
import UMC.career_mate.global.response.exception.code.CommonErrorCode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record CreatePlannerListDTO(

        @NotNull(message = "플래너 목록은 필수입니다.")
        @Valid
        List<CreatePlannerDTO> planners
) {
    public CreatePlannerListDTO {

        if (planners == null || planners.size() != 2) {
            throw new GeneralException(CommonErrorCode.INVALID_PLANNER_COUNT);
        }
    }
}