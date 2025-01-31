package UMC.career_mate.domain.answer.service;

import UMC.career_mate.domain.answer.Answer;
import UMC.career_mate.domain.answer.converter.AnswerConverter;
import UMC.career_mate.domain.answer.dto.request.AnswerCreateOrUpdateDTO;
import UMC.career_mate.domain.answer.dto.request.AnswerCreateOrUpdateDTO.AnswerInfo;
import UMC.career_mate.domain.answer.dto.request.AnswerCreateOrUpdateDTO.AnswerList;
import UMC.career_mate.domain.answer.repository.AnswerRepository;
import UMC.career_mate.domain.member.Member;
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
    public void saveAnswerList(Member member, AnswerCreateOrUpdateDTO answerCreateOrUpdateDTO) {
        long start_sequence = 1L;

        for (AnswerList answerList : answerCreateOrUpdateDTO.answerList()) {
            for (AnswerInfo answerInfo : answerList.answerInfoList()) {
                Question question = questionRepository.findById(answerInfo.questionId())
                        .orElseThrow(() -> new GeneralException(CommonErrorCode.NOT_FOUND_QUESTION));

                Answer answer = AnswerConverter.toAnswer(answerInfo, member, question, start_sequence);
                answerRepository.save(answer);
            }
            start_sequence++;
        }
    }

    @Transactional
    public void updateAnswerList(Member member, AnswerCreateOrUpdateDTO request) {
        for (AnswerList answerList : request.answerList()) {
            for (AnswerInfo answerInfo : answerList.answerInfoList()) {
                Question question = questionRepository.findById(answerInfo.questionId())
                        .orElseThrow(() -> new GeneralException(CommonErrorCode.NOT_FOUND_QUESTION));

                // 해당 회원, 질문, 시퀀스를 기준으로 기존 답변 조회
                Answer existingAnswer = answerRepository.findByMemberAndQuestionAndSequence(member, question, answerList.sequence())
                        .orElseThrow(() -> new GeneralException(CommonErrorCode.NOT_FOUND_ANSWER));

                existingAnswer.updateContent(answerInfo.content());

                // 질문 order 1의 답변 sequence 1은 수정일을 매번 업데이트 -> recruit 조회 로직에서 사용
                if (question.getOrder() == 1 && existingAnswer.getSequence() == 1) {
                    existingAnswer.setUpdatedAt();
                }
            }
        }
    }
}
