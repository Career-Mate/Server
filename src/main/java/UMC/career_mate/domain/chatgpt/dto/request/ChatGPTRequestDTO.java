package UMC.career_mate.domain.chatgpt.dto.request;

import lombok.Builder;

// TODO: 더미데이터용, 삭제 예정
@Builder
public record ChatGPTRequestDTO(
    String name,
    String content
) {

}
