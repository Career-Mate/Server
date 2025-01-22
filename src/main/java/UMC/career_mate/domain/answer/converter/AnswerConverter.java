package UMC.career_mate.domain.answer.converter;

import UMC.career_mate.domain.answer.Answer;
import UMC.career_mate.domain.answer.dto.response.AnswerInfo;
import UMC.career_mate.domain.question.Question;
import UMC.career_mate.domain.question.dto.response.QuestionDTO;

public class AnswerConverter {

    public static Answer toAnswer(AnswerInfo answerInfo, Question question) {
        return Answer.builder()
                .content(answerInfo.content())
                .question(question)
                .build();
    }
}
