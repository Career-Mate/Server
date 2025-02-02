package UMC.career_mate.domain.recruit.converter;

import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.recruit.Recruit;
import UMC.career_mate.domain.recruit.dto.FilterConditionDTO;
import UMC.career_mate.domain.recruit.dto.MemberTemplateAnswerDTO;
import UMC.career_mate.domain.recruit.dto.api.SaraminResponseDTO.Job;
import UMC.career_mate.domain.recruit.dto.response.RecommendRecruitsDTO;
import UMC.career_mate.domain.recruit.dto.response.RecommendRecruitsDTO.RecruitThumbNailInfoDTO;
import UMC.career_mate.domain.recruit.dto.response.RecruitInfoDTO;
import UMC.career_mate.domain.recruit.enums.EducationLevel;
import UMC.career_mate.domain.recruit.enums.RecruitKeyword;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
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

    public static RecommendRecruitsDTO toRecommendRecruitsDTO(Member member, List<RecruitThumbNailInfoDTO> recruitThumbNailInfoDTOList) {
        return RecommendRecruitsDTO.builder()
            .jobName(member.getJob().getName())
            .recruitThumbNailInfoDTOList(recruitThumbNailInfoDTOList)
            .build();
    }

    public static RecruitThumbNailInfoDTO toRecruitThumbNailInfoDTO(Recruit recruit, boolean isScraped) {
        return RecruitThumbNailInfoDTO.builder()
            .recruitId(recruit.getId())
            .companyName(recruit.getCompanyName())
            .title(recruit.getTitle())
            .deadLine(formatDeadLine(recruit))
            .isScraped(isScraped)
            .experienceLevelCode(recruit.getExperienceLevelCode())
            .experienceLevelMin(recruit.getExperienceLevelMin())
            .experienceLevelMax(recruit.getExperienceLevelMax())
            .experienceLevelName(recruit.getExperienceLevelName())
            .educationLevelCode(recruit.getEducationLevelCode())
            .educationLevelName(recruit.getEducationLevelName())
            .postingDate(recruit.getPostingDate())
            .build();
    }

    public static RecruitInfoDTO toRecruitInfoDTO(String comment, Recruit recruit) {
        return RecruitInfoDTO.builder()
            .comment(comment)
            .companyName(recruit.getCompanyName())
            .employmentName(recruit.getEmploymentName())
            .experienceLevelName(recruit.getExperienceLevelName())
            .educationLevelName(recruit.getEducationLevelName())
            .salaryName(recruit.getSalaryName())
            .region(recruit.getRegion())
            .companyInfoUrl(recruit.getCompanyInfoUrl())
            .recruitUrl(recruit.getRecruitUrl())
            .build();
    }

    public static FilterConditionDTO toFilterConditionDTO(RecruitKeyword recruitKeyword, int careerYear) {
        return FilterConditionDTO.builder()
            .recruitKeyword(recruitKeyword)
            .careerYear(careerYear)
            .build();
    }

    public static MemberTemplateAnswerDTO toMemberTemplateAnswerDTO(Member member, String content) {
        return MemberTemplateAnswerDTO.builder()
            .name("이름 : " + member.getName())
            .content(content)
            .build();
    }

    private static String formatDeadLine(Recruit recruit) {
        LocalDate today = LocalDate.now();
        LocalDate targetDate = recruit.getDeadLine().toLocalDate();
        long daysBetween = ChronoUnit.DAYS.between(today, targetDate);

        if (daysBetween == 0) {
            return "오늘 마감";
        }

        return "D-" + daysBetween;
    }
}
