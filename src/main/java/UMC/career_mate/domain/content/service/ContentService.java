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
import UMC.career_mate.global.response.exception.GeneralException;
import UMC.career_mate.global.response.exception.code.CommonErrorCode;
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

    public void deleteContent(Long contentId) {
        // 컨텐츠 존재 여부 확인
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new GeneralException(CommonErrorCode.NOT_FOUND_CONTENT));

        contentRepository.delete(content);
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<List<ContentResponseDTO>> getContentsByJobId(int page, int size, Member member) {

        // 로그인한 사용자의 직무 ID 확인
        Long jobId = member.getJob().getId();
        if (jobId == null) {
            throw new GeneralException(CommonErrorCode.NOT_FOUND_BY_JOB_ID);
        }

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

        return PageResponseDTO.<List<ContentResponseDTO>>builder()
            .page(page)
            .hasNext(hasNext)
            .result(contentList)
            .build();
    }
}
