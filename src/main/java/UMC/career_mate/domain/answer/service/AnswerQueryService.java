package UMC.career_mate.domain.answer.service;

import UMC.career_mate.domain.answer.Answer;
import UMC.career_mate.domain.answer.converter.AnswerConverter;
import UMC.career_mate.domain.answer.dto.response.AnswerInfoListDTO;
import UMC.career_mate.domain.answer.repository.AnswerRepository;
import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.member.repository.MemberRepository;
import UMC.career_mate.domain.template.enums.TemplateType;
import UMC.career_mate.global.common.PageResponseDTO;
import UMC.career_mate.global.response.exception.GeneralException;
import UMC.career_mate.global.response.exception.code.CommonErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerQueryService {
    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public PageResponseDTO<List<AnswerInfoListDTO>> getAnswersByTemplateType(Long memberId, int page, int size, TemplateType templateType) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(CommonErrorCode.BAD_REQUEST));

        Page<Answer> answerPage = answerRepository.findByMemberAndTemplateType(member, templateType, pageRequest);

        // sequence 기준으로 그룹화
        Map<Long, List<Answer>> groupedAnswers = answerPage.stream()
                .collect(Collectors.groupingBy(Answer::getSequence));

        // 그룹화된 데이터를 DTO로 변환
        List<AnswerInfoListDTO> answerListDTOList = groupedAnswers.entrySet().stream()
                .map(entry -> AnswerConverter.toAnswerListResponseDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return PageResponseDTO.<List<AnswerInfoListDTO>>builder()
                .page(pageRequest.getPageNumber() + 1)
                .hasNext(answerPage.hasNext())
                .result(answerListDTOList)
                .build();
    }
}
