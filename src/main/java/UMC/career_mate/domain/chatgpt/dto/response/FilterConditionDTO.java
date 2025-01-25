package UMC.career_mate.domain.chatgpt.dto.response;

import UMC.career_mate.domain.recruit.enums.RecruitKeyword;
import lombok.Builder;

@Builder
public record FilterConditionDTO(
    RecruitKeyword recruitKeyword,
    int careerYear
) {

}
