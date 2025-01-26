package UMC.career_mate.domain.contentScrap.dto.response;

import lombok.Builder;

@Builder
public record ContentScrapResponseDTO(
        Long contentId,
        String title,
        String url,
        String photo,
        boolean isScraped
) {
}