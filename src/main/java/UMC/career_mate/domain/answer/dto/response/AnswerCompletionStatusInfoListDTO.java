package UMC.career_mate.domain.answer.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record AnswerCompletionStatusInfoListDTO(
        boolean isAllCompleted,
        List<AnswerCompletionStatusInfoDTO> answerCompletionStatusInfoDTOList
) {

}
