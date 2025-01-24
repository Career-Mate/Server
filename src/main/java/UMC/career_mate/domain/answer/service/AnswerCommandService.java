package UMC.career_mate.domain.answer.service;

import UMC.career_mate.domain.answer.Answer;
import UMC.career_mate.domain.answer.converter.AnswerConverter;
import UMC.career_mate.domain.answer.dto.request.AnswerCreateOrUpdateDTO;
import UMC.career_mate.domain.answer.dto.request.AnswerCreateOrUpdateDTO.AnswerInfo;
import UMC.career_mate.domain.answer.dto.request.AnswerCreateOrUpdateDTO.AnswerList;
import UMC.career_mate.domain.answer.repository.AnswerRepository;
import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.member.repository.MemberRepository;
import UMC.career_mate.domain.question.Question;
import UMC.career_mate.domain.question.repository.QuestionRepository;
import UMC.career_mate.global.response.exception.GeneralException;
import UMC.career_mate.global.response.exception.code.CommonErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerCommandService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void saveAnswerList(Long memberId, AnswerCreateOrUpdateDTO answerCreateOrUpdateDTO) {
        long start_sequence = 1L;

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(CommonErrorCode.BAD_REQUEST));

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
    public void updateAnswerList(Long memberId, AnswerCreateOrUpdateDTO request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(CommonErrorCode.BAD_REQUEST));

        for (AnswerList answerList : request.answerList()) {
            for (AnswerInfo answerInfo : answerList.answerInfoList()) {
                Question question = questionRepository.findById(answerInfo.questionId())
                        .orElseThrow(() -> new GeneralException(CommonErrorCode.NOT_FOUND_QUESTION));

                // 해당 회원, 질문, 시퀀스를 기준으로 기존 답변 조회
                Answer existingAnswer = answerRepository.findByMemberAndQuestionAndSequence(member, question, answerList.sequence())
                        .orElseThrow(() -> new GeneralException(CommonErrorCode.NOT_FOUND_ANSWER));

                existingAnswer.updateContent(answerInfo.content());
            }
        }
    }
}
