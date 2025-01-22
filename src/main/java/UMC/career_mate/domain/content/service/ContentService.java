package UMC.career_mate.domain.content.service;

import UMC.career_mate.domain.content.Content;
import UMC.career_mate.domain.content.dto.request.ContentRequestDTO;
import UMC.career_mate.domain.content.dto.response.ContentResponseDTO;
import UMC.career_mate.domain.content.repository.ContentRepository;
import UMC.career_mate.domain.job.Job;
import UMC.career_mate.domain.job.repository.JobRepository;
import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.member.repository.MemberRepository;
import UMC.career_mate.global.response.exception.GeneralException;
import UMC.career_mate.global.response.exception.code.CommonErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;
    private final JobRepository jobRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ContentResponseDTO uploadContent(ContentRequestDTO contentRequestDTO) {
        // Job ID로 Job 엔티티 검색
        Job job = jobRepository.findById(contentRequestDTO.jobId())
                .orElseThrow(() -> new GeneralException(CommonErrorCode.NOT_FOUND_JOB));

        // Content 엔티티 생성 및 저장
        Content content = Content.builder()
                .title(contentRequestDTO.title())
                .url(contentRequestDTO.url())
                .photo(contentRequestDTO.photo())
                .job(job)
                .build();

        contentRepository.save(content);

        return ContentResponseDTO.fromEntity(content);
    }

    public List<ContentResponseDTO> getContentsByJobId(Long jobId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Content> contents = contentRepository.findByJobId(jobId, pageRequest);

        return contents.stream()
                .map(ContentResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }


}
