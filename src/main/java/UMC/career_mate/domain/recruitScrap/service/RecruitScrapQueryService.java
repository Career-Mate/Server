package UMC.career_mate.domain.recruitScrap.service;

import UMC.career_mate.domain.job.Job;
import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.recruitScrap.RecruitScrap;
import UMC.career_mate.domain.recruitScrap.converter.RecruitScrapConverter;
import UMC.career_mate.domain.recruitScrap.dto.response.RecruitScrapResponseDTO;
import UMC.career_mate.domain.recruitScrap.dto.response.RecruitScrapResponseDTO.RecruitScrapThumbNailInfoDTO;
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

    public RecruitScrapResponseDTO findRecruitScrapList(Member member) {
        Job job = member.getJob();
        List<RecruitScrap> recruitScrapList = recruitScrapRepository.findByMemberAndJobName(member,
            job.getName());

        if (recruitScrapList.isEmpty()) {
            return RecruitScrapConverter.toEmptyRecruitScrapResponseDTO(job);
        }

        List<RecruitScrapThumbNailInfoDTO> recruitScrapThumbNailInfoDTOList = recruitScrapList.stream()
            .map(RecruitScrapConverter::toRecruitScrapThumbNailInfoDTO)
            .toList();

        return RecruitScrapConverter.toRecruitScrapResponseDTO(job,
            recruitScrapThumbNailInfoDTOList);
    }
}
