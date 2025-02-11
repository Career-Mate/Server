package UMC.career_mate.domain.answer.service;

import UMC.career_mate.domain.answer.Answer;
import UMC.career_mate.domain.answer.converter.AnswerConverter;
import UMC.career_mate.domain.answer.dto.request.AnswerCreateOrUpdateDTO;
import UMC.career_mate.domain.answer.dto.request.AnswerCreateOrUpdateDTO.AnswerGroupDTO;
import UMC.career_mate.domain.answer.dto.request.AnswerCreateOrUpdateDTO.AnswerInfoDTO;
import UMC.career_mate.domain.answer.repository.AnswerRepository;
import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.member.repository.MemberRepository;
import UMC.career_mate.domain.question.Question;
import UMC.career_mate.domain.question.repository.QuestionRepository;
import UMC.career_mate.global.response.exception.GeneralException;
import UMC.career_mate.global.response.exception.code.CommonErrorCode;
import UMC.career_mate.global.s3.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnswerCommandService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final S3Uploader s3Uploader;

    private static final int MAX_ANSWERS_PER_QUESTION = 2;
    private final MemberRepository memberRepository;

    @Transactional
    public void saveAnswerList(Member member, AnswerCreateOrUpdateDTO answerCreateOrUpdateDTO, MultipartFile image1, MultipartFile image2) throws IOException {
        Map<Long, String> imageMap = new HashMap<>();
        if (image1 != null && !image1.isEmpty()) {
            imageMap.put(1L, s3Uploader.uploadImage(image1));
        }

        if (image2 != null && !image2.isEmpty()) {
            imageMap.put(2L, s3Uploader.uploadImage(image2));
        }

        long sequence = 1L;
        for (AnswerGroupDTO answerGroupDTO : answerCreateOrUpdateDTO.answerGroupDTOList()) {
            String assignedImageUrl = imageMap.getOrDefault(sequence, null);

            for (AnswerInfoDTO answerInfo : answerGroupDTO.answerInfoDTOList()) {
                Question question = questionRepository.findById(answerInfo.questionId())
                        .orElseThrow(() -> new GeneralException(CommonErrorCode.NOT_FOUND_QUESTION));

                validateAnswerLimit(question.getId());

                // 특정 질문에 대해서만 이미지 URL 저장
                String content = (assignedImageUrl != null && question.getId().equals(103L))
                        ? assignedImageUrl
                        : answerInfo.content();

                Answer answer = AnswerConverter.toAnswer(content, member, question, sequence);
                answerRepository.save(answer);
            }
            sequence++;
        }
    }

    private void validateAnswerLimit(Long questionId) {
        Long existingAnswerCount = answerRepository.countByQuestionId(questionId);
        if (existingAnswerCount >= MAX_ANSWERS_PER_QUESTION) {
            throw new GeneralException(CommonErrorCode.ALREADY_SAVE_ANSWER);
        }
    }

    @Transactional
    public void updateAnswerList(Member member, AnswerCreateOrUpdateDTO answerCreateOrUpdateDTO, MultipartFile image1, MultipartFile image2) throws IOException {
        Map<Long, String> imageMap = new HashMap<>();
        if (image1 != null && !image1.isEmpty()) {
            imageMap.put(1L, s3Uploader.uploadImage(image1));
        }

        if (image2 != null && !image2.isEmpty()) {
            imageMap.put(2L, s3Uploader.uploadImage(image2));
        }

        for (AnswerGroupDTO answerGroupDTO : answerCreateOrUpdateDTO.answerGroupDTOList()) {
            String assignedImageUrl = imageMap.get(answerGroupDTO.sequence());

            for (AnswerInfoDTO answerInfoDTO : answerGroupDTO.answerInfoDTOList()) {
                Question question = questionRepository.findById(answerInfoDTO.questionId())
                        .orElseThrow(() -> new GeneralException(CommonErrorCode.NOT_FOUND_QUESTION));

                // 해당 회원, 질문, 시퀀스를 기준으로 기존 답변 조회
                Answer existingAnswer = answerRepository.findByMemberAndQuestionAndSequence(member, question, answerGroupDTO.sequence())
                        .orElseThrow(() -> new GeneralException(CommonErrorCode.NOT_FOUND_ANSWER));

                // 특정 질문에 대해서만 이미지 URL 저장
                String content = (assignedImageUrl != null && question.getId().equals(103L))
                        ? assignedImageUrl
                        : answerInfoDTO.content();

                existingAnswer.updateContent(content);

                // 질문 order 1의 답변 sequence 1은 수정일을 매번 업데이트 -> recruit 조회 로직에서 사용
                if (question.getOrder() == 1 && existingAnswer.getSequence() == 1) {
                    existingAnswer.setUpdatedAt();
                }
            }
        }
    }

}
