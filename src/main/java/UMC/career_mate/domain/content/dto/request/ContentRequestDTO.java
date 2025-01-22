package UMC.career_mate.domain.content.dto.request;

import lombok.Builder;

@Builder
public record ContentRequestDTO(
        String title,
        String url,
        String photo,
        Long jobId
) {
}
