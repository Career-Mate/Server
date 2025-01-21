package UMC.career_mate.domain.question.converter;

import UMC.career_mate.domain.question.Question;
import UMC.career_mate.domain.question.dto.response.QuestionDTO;

import java.util.List;
import java.util.stream.Collectors;

public class QuestionConverter {
    public static QuestionDTO toQuestionDTO(Question question) {
        return QuestionDTO.builder()
                .questionId(question.getId())
                .content(question.getContent())
                .order(question.getOrder())
                .isRequired(question.getIsRequired())
                .build();
    }

    public static List<QuestionDTO> toQuestionDTOList(List<Question> questionList) {
        return questionList.stream()
                .map(QuestionConverter::toQuestionDTO)
                .collect(Collectors.toList());
    }
}
