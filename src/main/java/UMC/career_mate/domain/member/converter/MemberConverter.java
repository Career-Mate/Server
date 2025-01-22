package UMC.career_mate.domain.member.converter;

import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.member.dto.request.JoinMemberDTO;
import UMC.career_mate.domain.member.dto.response.MemberInfoDTO;
import UMC.career_mate.domain.member.enums.EducationStatus;
import UMC.career_mate.domain.member.enums.MemberEducationLevel;
import UMC.career_mate.domain.member.enums.SocialType;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter {

    public static Member toEmptyEntity(String clientId, SocialType socialType) {
        return Member.builder()
                .clientId(clientId)
                .socialType(socialType)
                .is_complete(false)
                .build();
    }

    public static MemberInfoDTO toMemberInfo(Member member) {
        return MemberInfoDTO.builder()
                .name(member.getName())
                .email(member.getEmail())
                .educationLevel(member.getEducationLevel())
                .major(member.getMajor())
                .educationStatus(member.getEducationStatus())
                .job(member.getJob())
                .build();
    }
}
