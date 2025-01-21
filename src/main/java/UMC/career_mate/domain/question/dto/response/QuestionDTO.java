package UMC.career_mate.domain.question.dto.response;

import lombok.Builder;

@Builder
public record QuestionDTO (
        Long questionId,
        String content,
        Integer order,
        Boolean isRequired
) {
    
}
