package UMC.career_mate.domain.template.converter;

import UMC.career_mate.domain.question.Question;
import UMC.career_mate.domain.question.converter.QuestionConverter;
import UMC.career_mate.domain.question.dto.response.QuestionDTO;
import UMC.career_mate.domain.template.Template;
import UMC.career_mate.domain.template.dto.response.TemplateInfoDTO;
import UMC.career_mate.domain.template.dto.response.TemplateInfoListDTO;

import java.util.List;

public class TemplateConverter {
    public static TemplateInfoListDTO toTemplateResponseDTO(List<Question> questionList, Template template) {
        List<QuestionDTO> questionDTOList = QuestionConverter.toQuestionDTOList(questionList);

        TemplateInfoDTO templateInfoDTO = TemplateInfoDTO.builder()
                .templateId(template.getId())
                .templateType(template.getTemplateType().getDescription())
                .questionDTOList(questionDTOList)
                .build();

        return new TemplateInfoListDTO(List.of(templateInfoDTO));
    }
}
