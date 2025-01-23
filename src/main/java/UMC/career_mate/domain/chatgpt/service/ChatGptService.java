package UMC.career_mate.domain.chatgpt.service;

import UMC.career_mate.domain.chatgpt.dto.api.response.ChatCompletionResponse;
import UMC.career_mate.domain.chatgpt.dto.request.ChatGPTRequestDTO;
import UMC.career_mate.domain.chatgpt.dto.response.FilterConditionDTO;
import UMC.career_mate.domain.chatgpt.dto.api.request.GptRequest;
import UMC.career_mate.domain.chatgpt.dto.api.request.GptRequest.Message;
import UMC.career_mate.domain.recruit.enums.RecruitKeyword;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatGptService {

    @Value("${chat-gpt.service-key}")
    private String serviceKey;

    private static final String GPT_REQUEST_FORMAT_PREFIX_FOR_CAREER_YEAR =
        "다음 이력서 데이터를 기반으로 추천 직무와 총 경력을 계산해줘. " +
        "직무는 'BACKEND_SPRING', 'BACKEND_NODE', 'BACKEND_DJANGO', 'FRONTEND', 'DESIGNER', 'PM' 중 하나로 선택하고, " +
        "총 경력은 각 회사의 근무 기간을 더해서 소수 말고 정수로만 결과를 계산해줘. " +
        "응답은 JSON으로 {\"직무\": \"추천 직무\", \"경력\": \"총 경력(년차)\"} 형식으로 작성해줘.";

    private static final String GPT_REQUEST_FORMAT_PREFIX_FOR_COMMENT =
        " = 사용자 이름이고, 다음 이력서 데이터를 기반으로 사용자의 강점을 어필할 수 있고, " +
        "어떤 포지션이 어울리는지 어떤 경험을 어필하면 좋을지 그런 내용들로 추천 조언 문구를 생성해줘. " +
        "'~~한 경험이 있는 000님, ~~한 경험을 어필해보면 어때요?', 또는 '~~포지에 지원해보 건 어떨까요? ~~에 강점을 드러낼 수 있을 것 같아요.'" +
        "와 비슷한 형식이지만 꼭 이런 형식이 아니더라도 너만의 스타일로 문구를 생성해줘." +
        "말투는 \"입니다\"같은 딱딱말 말투는 사용하지 말고, \"요.\"같이 부드럽게 표현해줘. " +
        "답변은 문구만 답변해줘. 문구를 생성할 때 이력서 데이터의 회사 이름은 제외해줘. 답변에서 엔터나 - 같은건 빼줘.";

    public FilterConditionDTO getFilterCondition(ChatGPTRequestDTO chatGPTRequestDTO) {
        GptRequest gptRequest = createGptRequest(
            GPT_REQUEST_FORMAT_PREFIX_FOR_CAREER_YEAR + " " + chatGPTRequestDTO.content());

        ObjectMapper om = new ObjectMapper();
        String gptAnswer = getGptAnswer(om, gptRequest);

        // "직무"와 "경력" 추출
        return extractJobAndCareer(om, gptAnswer);
    }

    public String getComment(ChatGPTRequestDTO chatGPTRequestDTO) {
        GptRequest gptRequest = createGptRequest(
            chatGPTRequestDTO.name() + GPT_REQUEST_FORMAT_PREFIX_FOR_COMMENT
                + chatGPTRequestDTO.content());

        ObjectMapper om = new ObjectMapper();

        return getGptAnswer(om, gptRequest);
    }

    private GptRequest createGptRequest(String content) {
        Message userMessage = Message.builder()
            .role("user")
            .content(content)
            .build();

        return GptRequest.builder()
            .model("gpt-3.5-turbo")
            .stream(false)
            .messages(List.of(userMessage))
            .build();
    }

    private String getGptAnswer(ObjectMapper om, GptRequest gptRequest) {
        String requestBody = createRequestBody(om, gptRequest);

        ResponseEntity<String> response = requestToGpt(requestBody);

        ChatCompletionResponse chatCompletionResponse = null;
        try {
            chatCompletionResponse = om.readValue(response.getBody(), ChatCompletionResponse.class);
        } catch (JsonProcessingException e) {
            log.error("gpt 응답을 ChatCompletionResponse로 파싱 중 에러");
            throw new RuntimeException(e);
        }

        logGptUsage(chatCompletionResponse.usage()); // 사용량 로그 출력

        return parseGptResponse(chatCompletionResponse);
    }

    private String createRequestBody(ObjectMapper om, GptRequest gptRequest) {
        try {
            return om.writeValueAsString(gptRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private ResponseEntity<String> requestToGpt(String requestBody) {
        URI uri = createUri();
        HttpHeaders headers = createHttpHeaders();
        HttpEntity entity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.exchange(
            uri,
            HttpMethod.POST,
            entity,
            String.class);
    }

    private URI createUri() {
        DefaultUriBuilderFactory builder = new DefaultUriBuilderFactory();

        String uriString = builder.builder()
            .scheme("https")
            .host("api.openai.com")
            .path("/v1/chat/completions")
            .build()
            .toString();

        URI uri = URI.create(uriString);

        return uri;
    }

    private HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + serviceKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return headers;
    }

    private void logGptUsage(ChatCompletionResponse.Usage usage) {
        log.info("질문 요청에 소모된 토큰 : {}", usage.promptTokens());
        log.info("답변 생성에 소모된 토큰 : {}", usage.completionTokens());
        log.info("총 소모된 토큰 : {}", usage.totalTokens());
    }

    private String parseGptResponse(ChatCompletionResponse response) {
        return response.choices().get(0).message().content();
    }

    private FilterConditionDTO extractJobAndCareer(ObjectMapper om, String gptAnswer) {
        JsonNode rootNode = null;
        try {
            rootNode = om.readTree(gptAnswer);
        } catch (JsonProcessingException e) {
            log.error("직무 경력 gpt 답변 결과 파싱 중 에러");
            throw new RuntimeException(e);
        }

        String job = rootNode.get("직무").asText();
        String career = rootNode.get("경력").asText();
        int careerYear = Integer.parseInt(career.split("년")[0]);

        log.info("추천 직무: {}", job);
        log.info("추천 경력: {}", career);

        RecruitKeyword recruitKeyword = RecruitKeyword.getRecruitKeyword(job);

        return FilterConditionDTO.builder()
            .recruitKeyword(recruitKeyword)
            .careerYear(careerYear)
            .build();
    }
}
