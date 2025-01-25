package UMC.career_mate.domain.answer.dto.response;

import UMC.career_mate.domain.template.enums.TemplateType;
import lombok.Builder;

@Builder
public record AnswerCompletionStatusInfoDTO(
        TemplateType templateType,
        boolean isComplete
) {

}
