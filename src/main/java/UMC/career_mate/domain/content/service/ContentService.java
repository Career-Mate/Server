package UMC.career_mate.domain.content.service;


import UMC.career_mate.domain.content.Content;
import UMC.career_mate.domain.content.converter.ContentConverter;
import UMC.career_mate.domain.content.dto.request.ContentRequestDTO;
import UMC.career_mate.domain.content.dto.response.ContentResponseDTO;
import UMC.career_mate.domain.content.repository.ContentRepository;
import UMC.career_mate.domain.job.Service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ContentService {

    private final ContentRepository contentRepository;
    private final JobService jobService;
    private final ContentConverter contentConverter;

    public ContentResponseDTO uploadContent(ContentRequestDTO contentRequestDTO) {
        // Job ID 유효성 확인
        var job = jobService.findJobById(contentRequestDTO.jobId());

        // Content 엔티티 생성 및 저장
        Content content = contentConverter.toContent(contentRequestDTO, job);
        contentRepository.save(content);

        return contentConverter.toContentResponseDTO(content);
    }

    @Transactional(readOnly = true)
    public List<ContentResponseDTO> getContentsByJobId(Long jobId, int page, int size) {
        // Job 유효성 확인
        jobService.findJobById(jobId);

        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Content> contentPage = contentRepository.findByJobId(jobId, pageRequest);

        // 빈 페이지 처리
        if (contentPage.isEmpty()) {
            return Collections.emptyList();
        }

        return contentPage.stream()
                .map(contentConverter::toContentResponseDTO)
                .collect(Collectors.toList());
    }
}