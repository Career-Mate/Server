package UMC.career_mate.domain.recruit.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record RecruitInfoDTO(
    String companyName,
    String title,
    String industryName,
    String region,
    String employmentName,
    String experienceLevelName,
    String educationLevelName,
    String salaryName,
    LocalDateTime deadLine,
    List<String> jobNames,
    String companyInfoUrl,
    String recruitUrl
) {

}
