package UMC.career_mate.domain.contentScrap.service;

import UMC.career_mate.domain.content.Content;
import UMC.career_mate.domain.content.repository.ContentRepository;
import UMC.career_mate.domain.contentScrap.ContentScrap;
import UMC.career_mate.domain.contentScrap.converter.ContentScrapConverter;
import UMC.career_mate.domain.contentScrap.dto.response.ContentScrapResponseDTO;
import UMC.career_mate.domain.contentScrap.repository.ContentScrapRepository;
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
public class ContentScrapService {

    private final ContentScrapRepository contentScrapRepository;
    private final ContentRepository contentRepository;

    public void createScrapContents(Member member, Long contentId) {

        // 유효한 컨텐츠인지 검사
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new GeneralException(CommonErrorCode.NOT_FOUND_CONTENT));

        // 중복 검사
        if (contentScrapRepository.findByContentIdAndMember(contentId, member).isPresent()) {
            throw new GeneralException(CommonErrorCode.DUPLICATE_SCRAP);
        }

        ContentScrap scrap = ContentScrapConverter.toContentScrap(content, member);
        contentScrapRepository.save(scrap);
    }

    public void deleteScrapContents(Long contentId, Member member) {

        //유효한 스크랩인지 검사
        ContentScrap scrap = contentScrapRepository.findByContentIdAndMember(contentId, member)
                .orElseThrow(() -> new GeneralException(CommonErrorCode.NOT_FOUND_SCRAP));

        contentScrapRepository.delete(scrap);
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<List<ContentScrapResponseDTO>> getScrapContents(Member member, int page, int size) {

        // 로그인한 사용자의 직무 ID 확인
        Long jobId = member.getJob().getId();
        if (jobId == null) {
            throw new GeneralException(CommonErrorCode.NOT_FOUND_BY_JOB_ID);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, size);

        // 로그인한 사용자가 스크랩한 콘텐츠 중 현재 직무 ID와 일치하는 것만 필터링
        Page<ContentScrap> scraps = contentScrapRepository.findByMemberAndJobId(member, jobId, pageRequest);

        List<ContentScrapResponseDTO> contentList = scraps.stream()
                .map(ContentScrapConverter::toContentScrapResponseDTO)
                .toList();

        return PageResponseDTO.<List<ContentScrapResponseDTO>>builder()
            .page(page)
            .hasNext(scraps.hasNext())
            .result(contentList)
            .build();
    }
}