package UMC.career_mate.domain.recruitScrap.repository;

import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.recruit.Recruit;
import UMC.career_mate.domain.recruitScrap.RecruitScrap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecruitScrapRepository extends JpaRepository<RecruitScrap, Long> {

    boolean existsByMemberAndRecruit(Member member, Recruit recruit);

    Optional<RecruitScrap> findByMemberAndRecruitId(Member member, Long recruitId);

    @Query("select rs from RecruitScrap rs join fetch rs.recruit where rs.member = :member")
    List<RecruitScrap> findByMember(@Param("member") Member member);

    @Query("select rs.recruit.id from RecruitScrap rs where rs.member = :member")
    Set<Long> findRecruitIdsByMember(@Param("member") Member member);
}
