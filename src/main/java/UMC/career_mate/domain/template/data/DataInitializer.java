package UMC.career_mate.domain.template.data;

import UMC.career_mate.domain.job.Job;
import UMC.career_mate.domain.job.repository.JobRepository;
import UMC.career_mate.domain.template.Template;
import UMC.career_mate.domain.template.enums.TemplateType;
import UMC.career_mate.domain.template.repository.TemplateRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DataInitializer {

    private final JobRepository jobRepository;
    private final TemplateRepository templateRepository;

    @PostConstruct
    @Transactional
    public void initializeData() {
        // 모든 직무를 조회
        List<Job> jobs = jobRepository.findAll();

        // 직무별로 템플릿 5개 추가
        jobs.forEach(job -> {
            // 각 직무에 대해 5개의 템플릿을 추가
            for (TemplateType templateType : TemplateType.values()) {
                Template template = new Template(
                        null,
                        templateType,
                        job
                );
                templateRepository.save(template);
            }
        });
    }
}
