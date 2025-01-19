package UMC.career_mate.domain.recruit.converter;

import UMC.career_mate.domain.recruit.Recruit;
import UMC.career_mate.domain.recruit.dto.api.SaraminResponseDTO.Job;
import UMC.career_mate.domain.recruit.dto.response.RecommendRecruitDTO;
import UMC.career_mate.domain.recruit.dto.response.RecruitInfoDTO;
import UMC.career_mate.domain.recruit.enums.EducationLevel;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;

@Slf4j
public class RecruitConverter {

    public static Recruit toRecruit(Job job, String recruitUrl, String companyInfoUrl) {
        String educationLevelName = job.position().requiredEducationLevel().name();
        EducationLevel educationLevel = EducationLevel.fromDescription(educationLevelName);

        return Recruit.builder()
            .companyName(job.company().detail().name())
            .title(job.position().title())
            .imageUrl(null)
            .deadLine(LocalDateTime.ofInstant(Instant.ofEpochSecond(job.expirationTimestamp()),
                ZoneId.systemDefault()))
            .companyInfoUrl(companyInfoUrl)
            .recruitUrl(recruitUrl)
            .experienceLevelCode(job.position().experienceLevel().code())
            .experienceLevelMin(job.position().experienceLevel().min())
            .experienceLevelMax(job.position().experienceLevel().max())
            .experienceLevelName(job.position().experienceLevel().name())
            .educationLevelCode(educationLevel.getCode())
            .educationLevelName(educationLevel.getDescription())
            .employmentName(job.position().jobType().name())
            .salaryName(job.salary().name())
            .jobNames(Arrays.asList(job.position().jobCode().name()))
            .region(StringEscapeUtils.unescapeHtml4(
                job.position().location().name()))
            .industryName(job.position().industry().name())
            .postingDate(LocalDateTime.ofInstant(Instant.ofEpochSecond(job.postingTimestamp()),
                ZoneId.systemDefault()))
            .openingDate(LocalDateTime.ofInstant(Instant.ofEpochSecond(job.openingTimestamp()),
                ZoneId.systemDefault()))
            .build();
    }

    public static RecommendRecruitDTO toRecommendRecruitDTO(Recruit recruit) {
        return RecommendRecruitDTO.builder()
            .recruitId(recruit.getId())
            .companyName(recruit.getCompanyName())
            .imageUrl(recruit.getImageUrl())
            .title(recruit.getTitle())
            .deadLine(recruit.getDeadLine())
            .experienceLevelCode(recruit.getExperienceLevelCode())
            .experienceLevelMin(recruit.getExperienceLevelMin())
            .experienceLevelMax(recruit.getExperienceLevelMax())
            .experienceLevelName(recruit.getExperienceLevelName())
            .educationLevelCode(recruit.getEducationLevelCode())
            .educationLevelName(recruit.getEducationLevelName())
            .openingDate(recruit.getOpeningDate())
            .postingDate(recruit.getPostingDate())
            .build();
    }

    public static RecruitInfoDTO toRecruitInfoDTO(Recruit recruit) {
        return RecruitInfoDTO.builder()
            .companyName(recruit.getCompanyName())
            .title(recruit.getTitle())
            .industryName(recruit.getIndustryName())
            .region(recruit.getRegion())
            .employmentName(recruit.getEmploymentName())
            .experienceLevelName(recruit.getExperienceLevelName())
            .educationLevelName(recruit.getEducationLevelName())
            .salaryName(recruit.getSalaryName())
            .deadLine(recruit.getDeadLine())
            .jobNames(recruit.getJobNames())
            .companyInfoUrl(recruit.getCompanyInfoUrl())
            .recruitUrl(recruit.getRecruitUrl())
            .build();
    }
}
