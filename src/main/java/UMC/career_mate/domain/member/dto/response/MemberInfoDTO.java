package UMC.career_mate.domain.member.dto.response;

import UMC.career_mate.domain.job.Job;
import UMC.career_mate.domain.member.enums.EducationStatus;
import UMC.career_mate.domain.member.enums.MemberEducationLevel;
import lombok.Builder;

@Builder
public record MemberInfoDTO(
       String name,
       String email,
       MemberEducationLevel educationLevel,
       String major,
       EducationStatus educationStatus,
       Job job
) {
}
