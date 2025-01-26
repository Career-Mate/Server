package UMC.career_mate.domain.recruitScrap.converter;

import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.recruit.Recruit;
import UMC.career_mate.domain.recruitScrap.RecruitScrap;
import UMC.career_mate.domain.recruitScrap.dto.response.RecruitScrapResponseDTO;

public class RecruitScrapConverter {

    public static RecruitScrap toEntity(Member member, Recruit recruit) {
        return RecruitScrap.builder()
            .member(member)
            .recruit(recruit)
            .build();
    }

    public static RecruitScrapResponseDTO toRecruitScrapResponseDTO(Recruit recruit) {
        return RecruitScrapResponseDTO.builder()
            .recruitId(recruit.getId())
            .companyName(recruit.getCompanyName())
            .imageUrl(null)
            .title(recruit.getTitle())
            .deadLine(recruit.getDeadLine())
            .isScraped(true)
            .build();
    }
}
