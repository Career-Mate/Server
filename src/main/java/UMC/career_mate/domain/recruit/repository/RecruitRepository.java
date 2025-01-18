package UMC.career_mate.domain.recruit.repository;

import UMC.career_mate.domain.recruit.Recruit;
import UMC.career_mate.domain.recruit.repository.querydsl.RecruitQueryRepository;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecruitRepository extends JpaRepository<Recruit, Long>, RecruitQueryRepository {

    @Query("select r.recruitUrl from Recruit r")
    Set<String> findRecruitUrls();

}
