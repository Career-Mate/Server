package UMC.career_mate.domain.answer.service;

import UMC.career_mate.domain.answer.Answer;
import UMC.career_mate.domain.answer.converter.AnswerConverter;
import UMC.career_mate.domain.answer.dto.response.AnswerCompletionStatusInfoDTO;
import UMC.career_mate.domain.answer.dto.response.AnswerCompletionStatusInfoListDTO;
import UMC.career_mate.domain.answer.dto.response.AnswerInfoListDTO;
import UMC.career_mate.domain.answer.repository.AnswerRepository;
import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.member.repository.MemberRepository;
import UMC.career_mate.domain.template.enums.TemplateType;
import UMC.career_mate.global.response.exception.GeneralException;
import UMC.career_mate.global.response.exception.code.CommonErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerQueryService {
    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<AnswerInfoListDTO> getAnswersByTemplateType(Long memberId, TemplateType templateType) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(CommonErrorCode.BAD_REQUEST));

        List<Answer> answerList = answerRepository.findByMemberAndTemplateType(member, templateType);

        // sequence 기준으로 그룹화
        Map<Long, List<Answer>> groupedAnswers = answerList.stream()
                .collect(Collectors.groupingBy(Answer::getSequence));

        return groupedAnswers.entrySet().stream()
                .map(entry -> AnswerConverter.toAnswerListResponseDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AnswerCompletionStatusInfoListDTO getAnswerCompletionStatus(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(CommonErrorCode.BAD_REQUEST));

        List<AnswerCompletionStatusInfoDTO> answerCompletionStatusInfoDTOList = new ArrayList<>();
        boolean isAllCompleted = true;

        // TemplateType 값들을 순회하면서 각 템플릿의 완료 여부를 확인
        for (TemplateType templateType : TemplateType.values()) {
            boolean isCompleted = answerRepository.existsByMemberAndTemplateType(member, templateType);
            answerCompletionStatusInfoDTOList.add(AnswerConverter.toAnswerCompletionStatusInfoDTO(templateType, isCompleted));

            // 하나라도 미완료 상태가 있으면 전체 진행 상태는 false
            if (!isCompleted) {
                isAllCompleted = false;
            }
        }

        return AnswerConverter.toAnswerCompletionStatusInfoListDTO(isAllCompleted, answerCompletionStatusInfoDTOList);
    }
}
