package UMC.career_mate.domain.content.converter;

import UMC.career_mate.domain.content.Content;
import UMC.career_mate.domain.content.dto.request.ContentRequestDTO;
import UMC.career_mate.domain.content.dto.response.ContentResponseDTO;
import UMC.career_mate.domain.job.Job;

public class ContentConverter {

    public static Content toContent(ContentRequestDTO contentRequestDTO, Job job) {
        return Content.builder()
                .title(contentRequestDTO.title())
                .url(contentRequestDTO.url())
                .photo(contentRequestDTO.photo())
                .job(job)
                .build();
    }

    public static ContentResponseDTO toContentResponseDTO(Content content) {
        return ContentResponseDTO.builder()
                .id(content.getId())
                .title(content.getTitle())
                .url(content.getUrl())
                .photo(content.getPhoto())
                .jobId(content.getJob().getId())
                .build();
    }
}