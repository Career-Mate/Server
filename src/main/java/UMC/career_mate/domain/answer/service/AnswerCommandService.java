package UMC.career_mate.domain.answer.service;

import UMC.career_mate.domain.answer.Answer;
import UMC.career_mate.domain.answer.converter.AnswerConverter;
import UMC.career_mate.domain.answer.dto.request.AnswerRequest;
import UMC.career_mate.domain.answer.dto.response.AnswerInfo;
import UMC.career_mate.domain.answer.dto.response.AnswerResponse;
import UMC.career_mate.domain.answer.repository.AnswerRepository;
import UMC.career_mate.domain.question.Question;
import UMC.career_mate.domain.question.repository.QuestionRepository;
import UMC.career_mate.global.response.exception.GeneralException;
import UMC.career_mate.global.response.exception.code.CommonErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnswerCommandService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public void saveAnswerList(AnswerRequest request) {
        for (AnswerResponse answerResponse : request.answerList()) {
            for (AnswerInfo answerInfo : answerResponse.answerInfoList()) {
                Question question = questionRepository.findById(answerInfo.questionId())
                        .orElseThrow(() -> new GeneralException(CommonErrorCode.NOT_FOUND_QUESTION));

                Answer answer = AnswerConverter.toAnswer(answerInfo, question);
                answerRepository.save(answer);
            }
        }
    }

//    private void validateAnswerLimit(Long questionId) {
//        Integer existingAnswerCount = answerRepository.countByQuestionId(questionId);
//        if (existingAnswerCount >= 2) {
//            throw new GeneralException(CommonErrorCode.TOO_MANY_ANSWERS);
//        }
//    }
}
