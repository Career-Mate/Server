package UMC.career_mate.domain.member.repository;

import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.member.enums.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Boolean existsMemberByClientIdAndSocialType(String clientId, SocialType socialType);

    Optional<Member> findMemberByClientIdAndSocialType(String clientId, SocialType socialType);

    Optional<Member> findMemberByIdAndClientId(Long id, String clientId);

    @Query("SELECT m FROM Member m where m.deletedAt is not null")
    List<Member> findDeleteMembers();
}
