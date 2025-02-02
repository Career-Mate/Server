package UMC.career_mate.domain.answer.dto.request;

import java.util.List;

public record AnswerCreateOrUpdateDTO(List<AnswerGroupDTO> answerGroupDTOList) {

    public record AnswerGroupDTO(Long sequence, List<AnswerInfoDTO> answerInfoDTOList) {
    }

    public record AnswerInfoDTO(Long questionId, String content) {
    }

}

