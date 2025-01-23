package UMC.career_mate.domain.content.dto.response;

import UMC.career_mate.domain.content.Content;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContentResponseDTO {
    private Long id;
    private String title;
    private String url;
    private String photo;
    private Long jobId;
}