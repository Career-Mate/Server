package UMC.career_mate.domain.content.dto.response;

import lombok.Builder;

@Builder
public record ContentResponseDTO(
        Long id,
        String title,
        String url,
        String photo,
        Long jobId,
        boolean isScrapped // 추가
) {
}