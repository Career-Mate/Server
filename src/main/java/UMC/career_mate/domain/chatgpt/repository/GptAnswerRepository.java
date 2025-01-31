package UMC.career_mate.domain.chatgpt.repository;

import UMC.career_mate.domain.chatgpt.GptAnswer;
import UMC.career_mate.domain.member.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GptAnswerRepository extends JpaRepository<GptAnswer, Long> {

    Optional<GptAnswer> findByMember(Member member);
}
