package UMC.career_mate.domain.planner.repository;

import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.planner.Planner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlannerRepository extends JpaRepository<Planner,Long> {

    Optional<Planner> findPlannerByMember(Member member);
}
