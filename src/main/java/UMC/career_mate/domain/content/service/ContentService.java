package UMC.career_mate.domain.content.service;


import UMC.career_mate.domain.content.Content;
import UMC.career_mate.domain.content.converter.ContentConverter;
import UMC.career_mate.domain.content.dto.request.ContentRequestDTO;
import UMC.career_mate.domain.content.dto.response.ContentResponseDTO;
import UMC.career_mate.domain.content.repository.ContentRepository;
import UMC.career_mate.domain.contentScrap.repository.ContentScrapRepository;
import UMC.career_mate.domain.job.Job;
import UMC.career_mate.domain.job.Service.JobService;
import UMC.career_mate.domain.member.Member;
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
    private final ContentScrapRepository contentScrapRepository;

    public ContentResponseDTO uploadContent(ContentRequestDTO contentRequestDTO) {
        // Job ID 유효성 확인
        Job job = jobService.findJobById(contentRequestDTO.jobId());

        // Content 엔티티 생성 및 저장
        Content content = ContentConverter.toContent(contentRequestDTO, job);
        contentRepository.save(content);

        return ContentConverter.toContentResponseDTO(content);
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<List<ContentResponseDTO>> getContentsByJobId(Long jobId, int page, int size, Member member) {
        // Job ID 유효성 확인
        jobService.findJobById(jobId);

        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Content> contentPage = contentRepository.findByJobId(jobId, pageRequest);

        // 사용자가 스크랩한 콘텐츠들 id 가져오기
        List<Long> scrappedContentIds = contentScrapRepository.findByMember(member)
                .stream()
                .map(scrap -> scrap.getContent().getId())
                .toList();

        // 컨텐츠가 위 목록에 있는지 확인하며 isScrapped 값을 포함(true) 포함x(false) 설정
        List<ContentResponseDTO> contentList = contentPage.stream()
                .map(content -> ContentConverter.toContentResponseDTOWithScrapStatus(content, scrappedContentIds.contains(content.getId())))
                .toList();

        boolean hasNext = contentPage.hasNext();
        return new PageResponseDTO<>(page, hasNext, contentList);
    }
}