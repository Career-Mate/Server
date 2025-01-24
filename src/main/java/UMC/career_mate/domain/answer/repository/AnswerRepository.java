package UMC.career_mate.domain.answer.repository;

import UMC.career_mate.domain.answer.Answer;
import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.question.Question;
import UMC.career_mate.domain.template.enums.TemplateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Query("""
            SELECT a FROM Answer a
            WHERE a.member = :member AND a.question.template.templateType = :templateType
    """)
    List<Answer> findByMemberAndTemplateType(@Param("member") Member member, @Param("templateType") TemplateType templateType);

    Optional<Answer> findByMemberAndQuestionAndSequence(Member member, Question question, Long sequence);
}
