package UMC.career_mate.domain.recruitScrap.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record RecruitScrapResponseDTO(
    Long recruitId,
    String companyName,
    String imageUrl,
    String title,
    String deadLine,
    boolean isScraped
) {

}
