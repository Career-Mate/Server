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
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<ContentScrap> scraps = contentScrapRepository.findByMember(member, pageRequest);

        List<ContentScrapResponseDTO> contentList = scraps.stream()
                .map(ContentScrapConverter::toContentScrapResponseDTO)
                .toList();

        return new PageResponseDTO<>(page, scraps.hasNext(), contentList);
    }
}