package UMC.career_mate.domain.answer.dto.response;

import lombok.Builder;

@Builder
public record AnswerInfoDTO(
        Long questionId,
        String questionName,
        String content
) {

}
