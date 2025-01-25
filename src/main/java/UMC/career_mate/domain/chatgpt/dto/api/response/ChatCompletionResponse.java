package UMC.career_mate.domain.chatgpt.dto.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChatCompletionResponse(
    String id,
    String object,
    long created,
    String model,
    List<Choice> choices,
    Usage usage,
    @JsonProperty("system_fingerprint") String systemFingerprint
) {

    public record Choice(
        int index,
        Message message,
        String logprobs,
        @JsonProperty("finish_reason") String finishReason
    ) {
    }

    public record Message(
        String role,
        String content,
        String refusal
    ) {
    }

    public record Usage(
        @JsonProperty("prompt_tokens") int promptTokens,
        @JsonProperty("completion_tokens") int completionTokens,
        @JsonProperty("total_tokens") int totalTokens,
        @JsonProperty("prompt_tokens_details") PromptTokensDetails promptTokensDetails,
        @JsonProperty("completion_tokens_details") CompletionTokensDetails completionTokensDetails
    ) {
    }

    public record PromptTokensDetails(
        @JsonProperty("cached_tokens") int cachedTokens,
        @JsonProperty("audio_tokens") int audioTokens
    ) {
    }

    public record CompletionTokensDetails(
        @JsonProperty("reasoning_tokens") int reasoningTokens,
        @JsonProperty("rejected_prediction_tokens") int rejectedPredictionTokens,
        @JsonProperty("audio_tokens") int audioTokens,
        @JsonProperty("accepted_prediction_tokens") int acceptedPredictionTokens
    ) {
    }
}
