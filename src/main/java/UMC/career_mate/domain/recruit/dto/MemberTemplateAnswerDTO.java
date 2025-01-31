package UMC.career_mate.domain.recruit.dto;

import lombok.Builder;

@Builder
public record MemberTemplateAnswerDTO(
    String name,
    String content
) {

}
