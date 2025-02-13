package UMC.career_mate.domain.recruit.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record RecommendRecruitsDTO(
    String jobName,
    List<RecruitThumbNailInfoDTO> recruitThumbNailInfoDTOList
) {

    @Builder
    public record RecruitThumbNailInfoDTO(
        Long recruitId,
        String companyName,
        String title,
        String deadLine,
        boolean isScrapped
    ) {

    }

}
