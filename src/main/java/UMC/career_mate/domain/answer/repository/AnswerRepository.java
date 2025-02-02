package UMC.career_mate.domain.answer.repository;

import UMC.career_mate.domain.answer.Answer;
import UMC.career_mate.domain.job.Job;
import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.question.Question;
import UMC.career_mate.domain.template.enums.TemplateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Integer countByQuestionId(Long questionId);

    @Query("""
                    SELECT a FROM Answer a
                    WHERE a.member = :member
                    AND a.question.template.job = :job
                    AND a.question.template.templateType = :templateType
            """)
    List<Answer> findByMemberAndTemplateType(@Param("member") Member member,
                                             @Param("job") Job job,
                                             @Param("templateType") TemplateType templateType);

    Optional<Answer> findByMemberAndQuestionAndSequence(Member member, Question question, Long sequence);

    @Query("""
                SELECT COUNT(a) > 0 FROM Answer a
                WHERE a.member = :member
                AND a.question.template.job = :job
                AND a.question.template.templateType = :templateType
            """)
    boolean existsByMemberAndTemplateType(@Param("member") Member member,
                                          @Param("job") Job job,
                                          @Param("templateType") TemplateType templateType);

    @Query("select a from Answer a where a.member = :member and a.question.template.templateType in :templateTypes and a.question.order = :order and a.sequence = :sequence and a.question.template.job = :job")
    List<Answer> findByMemberAndTemplateTypesAndOrderAndJob(@Param("member") Member member,
                                                            @Param("templateTypes") List<TemplateType> templateTypes, @Param("order") int order,
                                                            @Param("sequence") int sequence, @Param("job") Job job);

    @Query("select a from Answer a where a.member = :member and a.question.content = :content and a.question.template.templateType = :templateType and a.question.template.job = :job")
    List<Answer> findAnswersByMemberAndQuestionContentAndTemplateTypeAndJob(
            @Param("member") Member member, @Param("content") String content,
            @Param("templateType") TemplateType templateType, @Param("job") Job job);

    @Query("select a from Answer a where a.member = :member and a.question.template.templateType in :templateTypes and a.question.template.job = :job")
    List<Answer> findByMemberAndTemplateTypesAndJob(@Param("member") Member member,
                                                    @Param("templateTypes") List<TemplateType> templateTypes, @Param("job") Job job);
}
