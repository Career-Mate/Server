package UMC.career_mate.domain.template.dto.response;

import UMC.career_mate.domain.question.dto.response.QuestionDTO;
import lombok.Builder;

import java.util.List;

@Builder
public record TemplateInfoDTO(
        Long templateId,
        String templateType,
        List<QuestionDTO> questionDTOList
) {

}
