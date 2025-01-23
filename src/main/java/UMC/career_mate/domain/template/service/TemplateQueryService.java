package UMC.career_mate.domain.template.service;

import UMC.career_mate.domain.job.Job;
import UMC.career_mate.domain.job.repository.JobRepository;
import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.member.repository.MemberRepository;
import UMC.career_mate.domain.question.Question;
import UMC.career_mate.domain.question.repository.QuestionRepository;
import UMC.career_mate.domain.template.Template;
import UMC.career_mate.domain.template.converter.TemplateConverter;
import UMC.career_mate.domain.template.dto.response.TemplateDTO;
import UMC.career_mate.domain.template.dto.response.TemplateResponseDTO;
import UMC.career_mate.domain.template.enums.TemplateType;
import UMC.career_mate.domain.template.repository.TemplateRepository;
import UMC.career_mate.global.response.exception.GeneralException;
import UMC.career_mate.global.response.exception.code.CommonErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TemplateQueryService {
    private final TemplateRepository templateRepository;
    private final JobRepository jobRepository;
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public TemplateResponseDTO getTemplate(Long memberId, TemplateType type) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(CommonErrorCode.BAD_REQUEST));

        Template template = templateRepository.findByJobAndTemplateType(member.getJob(), type)
                .orElseThrow(() -> new GeneralException(CommonErrorCode.NOT_FOUND_TEMPLATE));

        List<Question> questionList = questionRepository.findByTemplate(template);
        return TemplateConverter.toTemplateResponseDTO(questionList, template);
    }
}
