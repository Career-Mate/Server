package UMC.career_mate.domain.recruitScrap.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record RecruitScrapResponseDTO(
    String jobName,
    List<RecruitScrapThumbNailInfoDTO> recruitScrapThumbNailInfoDTOList
) {

    @Builder
    public record RecruitScrapThumbNailInfoDTO(
        Long recruitId,
        String companyName,
        String title,
        String deadLine,
        boolean isScrapped
        ){
    }
}
