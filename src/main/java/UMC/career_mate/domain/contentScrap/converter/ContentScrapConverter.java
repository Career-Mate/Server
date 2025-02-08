package UMC.career_mate.domain.contentScrap.converter;

import UMC.career_mate.domain.content.Content;
import UMC.career_mate.domain.contentScrap.ContentScrap;
import UMC.career_mate.domain.contentScrap.dto.response.ContentScrapResponseDTO;
import UMC.career_mate.domain.member.Member;

public class ContentScrapConverter {

    public static ContentScrap toContentScrap(Content content, Member member) {
        return ContentScrap.builder()
                .content(content)
                .member(member)
                .build();
    }

    public static ContentScrapResponseDTO toContentScrapResponseDTO(ContentScrap scrap) {
        return ContentScrapResponseDTO.builder()
                .contentId(scrap.getContent().getId())
                .title(scrap.getContent().getTitle())
                .url(scrap.getContent().getUrl())
                .photo(scrap.getContent().getPhoto())
                .isScrapped(true) //isScrapped 값이 참인 것들을 가져옵니다.
                .build();
    }
}