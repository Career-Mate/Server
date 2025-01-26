package UMC.career_mate.domain.contentScrap.repository;

import UMC.career_mate.domain.contentScrap.ContentScrap;
import UMC.career_mate.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ContentScrapRepository extends JpaRepository<ContentScrap, Long> {
    Optional<ContentScrap> findByContentIdAndMember(Long contentId, Member member);

    Page<ContentScrap> findByMember(Member member, Pageable pageable);

    //회원이 스크랩한 컨텐츠를 모두 조회
    @Query("SELECT cs FROM ContentScrap cs WHERE cs.member = :member")
    List<ContentScrap> findByMember(@Param("member") Member member);
}