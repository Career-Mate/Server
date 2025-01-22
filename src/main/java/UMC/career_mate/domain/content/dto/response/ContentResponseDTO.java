package UMC.career_mate.domain.content.dto.response;

import UMC.career_mate.domain.content.Content;
import lombok.Builder;

@Builder
public record ContentResponseDTO(
        Long id,
        String title,
        String url,
        String photo,
        Long jobId
) {
    public static ContentResponseDTO fromEntity(Content content) {
        return ContentResponseDTO.builder()
                .id(content.getId())
                .title(content.getTitle())
                .url(content.getUrl())
                .photo(content.getPhoto())
                .jobId(content.getJob().getId())
                .build();
    }
}
