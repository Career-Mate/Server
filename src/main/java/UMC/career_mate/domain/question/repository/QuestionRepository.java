package UMC.career_mate.domain.question.repository;

import UMC.career_mate.domain.question.Question;
import UMC.career_mate.domain.template.Template;
import UMC.career_mate.domain.template.enums.TemplateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByTemplate(Template template);
}
