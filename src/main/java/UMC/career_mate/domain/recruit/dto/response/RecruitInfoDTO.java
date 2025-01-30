package UMC.career_mate.domain.recruit.dto.response;

import lombok.Builder;

@Builder
public record RecruitInfoDTO(
    String comment,
    String companyName,
    String employmentName,
    String experienceLevelName,
    String educationLevelName,
    String salaryName,
    String region,
    String companyInfoUrl,
    String recruitUrl
) {

}
