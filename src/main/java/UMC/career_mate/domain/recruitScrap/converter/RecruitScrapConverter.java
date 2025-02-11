package UMC.career_mate.domain.recruitScrap.converter;

import UMC.career_mate.domain.job.Job;
import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.recruit.Recruit;
import UMC.career_mate.domain.recruitScrap.RecruitScrap;
import UMC.career_mate.domain.recruitScrap.dto.response.RecruitScrapResponseDTO;
import UMC.career_mate.domain.recruitScrap.dto.response.RecruitScrapResponseDTO.RecruitScrapThumbNailInfoDTO;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class RecruitScrapConverter {

    public static RecruitScrap toEntity(Member member, Recruit recruit) {
        return RecruitScrap.builder()
            .member(member)
            .recruit(recruit)
            .jobName(member.getJob().getName())
            .build();
    }

    public static RecruitScrapResponseDTO toRecruitScrapResponseDTO(Job job,
        List<RecruitScrapThumbNailInfoDTO> recruitScrapThumbNailInfoDTOList) {
        return RecruitScrapResponseDTO.builder()
            .jobName(job.getName())
            .recruitScrapThumbNailInfoDTOList(recruitScrapThumbNailInfoDTOList)
            .build();
    }

    public static RecruitScrapThumbNailInfoDTO toRecruitScrapThumbNailInfoDTO(RecruitScrap recruitScrap) {
        return RecruitScrapThumbNailInfoDTO.builder()
            .recruitId(recruitScrap.getRecruit().getId())
            .companyName(recruitScrap.getRecruit().getCompanyName())
            .title(recruitScrap.getRecruit().getTitle())
            .deadLine(formatDeadLine(recruitScrap.getRecruit()))
            .isScrapped(true)
            .build();
    }

    public static RecruitScrapResponseDTO toEmptyRecruitScrapResponseDTO(Job job) {
        return RecruitScrapResponseDTO.builder()
            .jobName(job.getName())
            .recruitScrapThumbNailInfoDTOList(List.of())
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
