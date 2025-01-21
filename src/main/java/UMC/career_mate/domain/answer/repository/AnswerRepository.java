package UMC.career_mate.domain.answer.repository;

import UMC.career_mate.domain.answer.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Integer countByQuestionId(Long questionId);
}
