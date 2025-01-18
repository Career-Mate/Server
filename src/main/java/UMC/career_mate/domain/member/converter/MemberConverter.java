package UMC.career_mate.domain.member.converter;

import UMC.career_mate.domain.job.Job;
import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.member.dto.request.JoinMemberDTO;
import UMC.career_mate.domain.member.enums.EducationStatus;
import UMC.career_mate.domain.member.enums.SocialType;
import UMC.career_mate.domain.recruit.enums.EducationLevel;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter {

    public static Member toEntity(JoinMemberDTO request, Job job) {
        return Member.builder()
                .name(request.name())
                .email(request.email())
                .educationLevel(EducationLevel.valueOf(request.educationLevel()))
                .educationStatus(EducationStatus.valueOf(request.educationStatus()))
                .job(job)
                .socialType(SocialType.valueOf(request.socialType()))
                .clientId(request.clientId())
                .build();
    }
}
