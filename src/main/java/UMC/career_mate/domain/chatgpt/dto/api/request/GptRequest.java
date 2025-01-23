package UMC.career_mate.domain.chatgpt.dto.api.request;

import java.util.List;
import lombok.Builder;

@Builder
public record GptRequest(
    String model,
    boolean stream,
    List<Message> messages
) {

    @Builder
    public record Message(
        String role,
        String content
    ) {
    }
}
