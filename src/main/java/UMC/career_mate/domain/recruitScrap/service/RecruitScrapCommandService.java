package UMC.career_mate.domain.recruitScrap.service;

import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.recruit.Recruit;
import UMC.career_mate.domain.recruit.repository.RecruitRepository;
import UMC.career_mate.domain.recruitScrap.RecruitScrap;
import UMC.career_mate.domain.recruitScrap.converter.RecruitScrapConverter;
import UMC.career_mate.domain.recruitScrap.repository.RecruitScrapRepository;
import UMC.career_mate.global.response.exception.GeneralException;
import UMC.career_mate.global.response.exception.code.CommonErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RecruitScrapCommandService {

    private final RecruitScrapRepository recruitScrapRepository;
    private final RecruitRepository recruitRepository;

    public void saveRecruitScrap(Member member, Long recruitId) {
        Recruit recruit = recruitRepository.findById(recruitId).orElseThrow(
            () -> new GeneralException(CommonErrorCode.NOT_FOUND_RECRUIT)
        );

        if (recruitScrapRepository.existsByMemberAndRecruit(member, recruit)) {
            throw new GeneralException(CommonErrorCode.DUPLICATE_RECRUIT_SCRAP);
        }

        RecruitScrap recruitScrap = RecruitScrapConverter.toEntity(member, recruit);

        recruitScrapRepository.save(recruitScrap);
    }

    public void deleteRecruitScrap(Member member, Long recruitId) {
        RecruitScrap recruitScrap = recruitScrapRepository.findByMemberAndRecruitId(member,
            recruitId).orElseThrow(
            () -> new GeneralException(CommonErrorCode.NOT_FOUND_RECRUIT_SCRAP)
        );

        recruitScrapRepository.delete(recruitScrap);
    }
}
