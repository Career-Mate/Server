package UMC.career_mate.domain.contentScrap.repository;

import UMC.career_mate.domain.contentScrap.ContentScrap;
import UMC.career_mate.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContentScrapRepository extends JpaRepository<ContentScrap, Long> {
    Optional<ContentScrap> findByContentIdAndMember(Long contentId, Member member);

    Page<ContentScrap> findByMember(Member member, Pageable pageable);
}