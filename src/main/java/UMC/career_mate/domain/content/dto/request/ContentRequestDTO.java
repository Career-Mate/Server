package UMC.career_mate.domain.content.dto.request;

public record ContentRequestDTO(
        String title,
        String url,
        String photo,
        Long jobId
) {
}
