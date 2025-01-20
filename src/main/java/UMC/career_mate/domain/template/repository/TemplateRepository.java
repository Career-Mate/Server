package UMC.career_mate.domain.template.repository;

import UMC.career_mate.domain.job.Job;
import UMC.career_mate.domain.template.Template;
import UMC.career_mate.domain.template.enums.TemplateType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TemplateRepository extends JpaRepository<Template, Long> {
    Optional<Template> findByJobAndTemplateType(Job job, TemplateType templateType);
}
