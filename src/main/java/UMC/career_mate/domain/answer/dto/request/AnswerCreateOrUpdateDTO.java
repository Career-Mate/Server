package UMC.career_mate.domain.answer.dto.request;

import java.util.List;

public record AnswerCreateOrUpdateDTO(List<AnswerList> answerList) {

    public record AnswerList(Long sequence, List<AnswerInfo> answerInfoList) {
    }

    public record AnswerInfo(Long questionId, String content) {
    }

}

