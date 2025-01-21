package UMC.career_mate.domain.member.repository;

import UMC.career_mate.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
