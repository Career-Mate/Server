package UMC.career_mate.domain.content.service;


import UMC.career_mate.domain.content.Content;
import UMC.career_mate.domain.content.converter.ContentConverter;
import UMC.career_mate.domain.content.dto.request.ContentRequestDTO;
import UMC.career_mate.domain.content.dto.response.ContentResponseDTO;
import UMC.career_mate.domain.content.repository.ContentRepository;
import UMC.career_mate.domain.job.Job;
import UMC.career_mate.domain.job.Service.JobService;
import UMC.career_mate.global.common.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ContentService {

    private final ContentRepository contentRepository;
    private final JobService jobService;

    public ContentResponseDTO uploadContent(ContentRequestDTO contentRequestDTO) {
        // Job ID 유효성 확인
        Job job = jobService.findJobById(contentRequestDTO.jobId());

        // Content 엔티티 생성 및 저장
        Content content = ContentConverter.toContent(contentRequestDTO, job);
        contentRepository.save(content);

        return ContentConverter.toContentResponseDTO(content);
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<List<ContentResponseDTO>> getContentsByJobId(Long jobId, int page, int size) {
        // Job 유효성 확인
        jobService.findJobById(jobId);

        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Content> contentPage = contentRepository.findByJobId(jobId, pageRequest);

        // 빈페이지를 따로 처리하지 않고 PageResponseDTO로 감싸서 반환하는 방식으로 바꾸었습니다!
        List<ContentResponseDTO> contentList = contentPage.stream()
                .map(ContentConverter::toContentResponseDTO)
                .toList();

        boolean hasNext = contentPage.hasNext();
        return new PageResponseDTO<>(page, hasNext, contentList);
    }
}