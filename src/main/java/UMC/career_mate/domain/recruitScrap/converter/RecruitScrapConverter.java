package UMC.career_mate.domain.recruitScrap.converter;

import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.recruit.Recruit;
import UMC.career_mate.domain.recruitScrap.RecruitScrap;
import UMC.career_mate.domain.recruitScrap.dto.response.RecruitScrapResponseDTO;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RecruitScrapConverter {

    public static RecruitScrap toEntity(Member member, Recruit recruit) {
        return RecruitScrap.builder()
            .member(member)
            .recruit(recruit)
            .build();
    }

    public static RecruitScrapResponseDTO toRecruitScrapResponseDTO(Recruit recruit, String jobName) {
        return RecruitScrapResponseDTO.builder()
            .recruitId(recruit.getId())
            .companyName(recruit.getCompanyName())
            .title(recruit.getTitle())
            .deadLine(formatDeadLine(recruit))
            .isScraped(true)
            .companyInfoUrl(recruit.getCompanyInfoUrl())
            .recruitUrl(recruit.getRecruitUrl())
            .jobName(jobName)
            .build();
    }

    private static String formatDeadLine(Recruit recruit) {
        LocalDate today = LocalDate.now();
        LocalDate targetDate = recruit.getDeadLine().toLocalDate();
        long daysBetween = ChronoUnit.DAYS.between(today, targetDate);

        if (daysBetween == 0) {
            return "오늘 마감";
        }

        if (daysBetween > 0) {
            return "D-" + daysBetween;
        }

        return "마감";
    }
}
