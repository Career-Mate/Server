package UMC.career_mate.domain.recruitScrap.service;

import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.recruitScrap.RecruitScrap;
import UMC.career_mate.domain.recruitScrap.converter.RecruitScrapConverter;
import UMC.career_mate.domain.recruitScrap.dto.response.RecruitScrapResponseDTO;
import UMC.career_mate.domain.recruitScrap.repository.RecruitScrapRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitScrapQueryService {

    private final RecruitScrapRepository recruitScrapRepository;

    public List<RecruitScrapResponseDTO> findRecruitScrapList(Member member) {
        List<RecruitScrap> recruitScrapList = recruitScrapRepository.findByMember(member);

        if (recruitScrapList.isEmpty()) {
            return List.of();
        }

        return recruitScrapList.stream()
            .map(RecruitScrap::getRecruit)
            .map(RecruitScrapConverter::toRecruitScrapResponseDTO)
            .toList();
    }
}
