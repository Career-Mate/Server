package UMC.career_mate.domain.chatgpt.service;

import UMC.career_mate.domain.chatgpt.dto.api.response.ChatCompletionResponse;
import UMC.career_mate.domain.job.Job;
import UMC.career_mate.domain.recruit.dto.MemberTemplateAnswerDTO;
import UMC.career_mate.domain.chatgpt.dto.api.request.GptRequest;
import UMC.career_mate.domain.chatgpt.dto.api.request.GptRequest.Message;
import UMC.career_mate.domain.recruit.enums.RecruitKeyword;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

    private static final String GPT_REQUEST_FORMAT_POSTFIX_FOR_CAREER_YEAR =
        "이 사람의 경력을 계산해서 앞뒤 설명 하지말고 정수로만 올바른 답변 예시와 같은 형식으로 답변해줘. " +
            "올바론 답변 예시) 5, 잘못된 답변 예시 1) 5년, 잘못된 답변 예시 2) 5 years, 잘못된 답변 예시 3) 이 사람의 경력은 5년";

    private static final String GPT_REQUEST_FORMAT_POSTFIX_FOR_RECRUIT_KEYWORD_BACKEND =
        "이 사람의 직무를 내가 제시한 보기들 중에서 하나만 골라서 답변해줘.\n" +
            "직무 보기 :'BACKEND_SPRING', 'BACKEND_NODE', 'BACKEND_DJANGO'. " +
            "이 중에서 적절한 직무를 고르기 애매하면 'BACKEND'로 답변해줘.\n" +
            "앞뒤 설명하지 말고 직무만 답변해줘.";

    private static final String GPT_REQUEST_FORMAT_POSTFIX_FOR_RECRUIT_KEYWORD_FRONTEND =
        "이 사람의 직무를 내가 제시한 보기들 중에서 하나만 골라서 답변해줘.\n" +
            "직무 보기 : 'FRONTEND_REACT', 'FRONTEND_IOS', 'FRONTEND_ANDROID'. " +
            "이 중에서 적절한 직무를 고르기 애매하면 'FRONTEND'로 답변해줘.\n" +
            "앞뒤 설명하지 말고 직무만 답변해줘.";

    private static final String GPT_REQUEST_FORMAT_FOR_COMMENT =
        "위 경험 데이터를 기반으로 사용자의 이름을 문구에 포함해서 사용자의 강점을 어필할 수 있고, " +
            "친근한 말투로 어떤 포지션이 어울리는지, 어떤 경험을 어필하면 좋을지와 같은 내용으로 조언 문구를 생성해서 답변해줘. " +
            "답변은 문구만 답변해줘. 문구를 생성할 때 내용에는 회사 이름은 제외해줘. " +
            "답변에서 '-'는 빼줘." +
            " 최종 내용을 3줄 요약해주고, 온점을 기준으로 한줄씩 들여쓰기해줘.";

    private static final Map<String, String> formatMap = Map.ofEntries(
        Map.entry("백엔드 개발자", GPT_REQUEST_FORMAT_POSTFIX_FOR_RECRUIT_KEYWORD_BACKEND),
        Map.entry("프론트엔드 개발자", GPT_REQUEST_FORMAT_POSTFIX_FOR_RECRUIT_KEYWORD_FRONTEND)
    );

    private static final String GPT_SYSTEM_ROLE = "너는 취업 전문가로서 내가 보낸 경험 데이터를 기반으로 '~~한 경험이 있는 000님, ~~한 경험을 어필해보면 어때요?' 라는 느낌으로 사용자 맞춤형 추천 문구를 작성한다. " +
        "문구의 말투는 '-니다'체를 사용하는 것이 아니라, '-요'체를 사용한다.";

    public int getCareerYear(String chatGptRequestContent) {
        GptRequest gptRequest = createGptRequest(
            chatGptRequestContent + GPT_REQUEST_FORMAT_POSTFIX_FOR_CAREER_YEAR);

        ObjectMapper om = new ObjectMapper();
        String gptAnswer = getGptAnswer(om, gptRequest);

        log.info("\ngptRequest : {}, gptAnswer : {} ", gptRequest, gptAnswer);

        return Integer.parseInt(gptAnswer);
    }

    public RecruitKeyword getRecruitKeyword(String chatGptRequestContent, Job job) {
        GptRequest gptRequest = createGptRequest(
            chatGptRequestContent + formatMap.get(job.getName()));

        ObjectMapper om = new ObjectMapper();
        String gptAnswer = getGptAnswer(om, gptRequest);

        log.info("\ngptRequest : {}\ngptAnswer : {} ", gptRequest, gptAnswer);

        return RecruitKeyword.getRecruitKeywordFromGptAnswerJob(gptAnswer);
    }

    public String getComment(MemberTemplateAnswerDTO memberTemplateAnswerDTO) {
        GptRequest gptRequest = createGptRequestWithSystemRole(
            memberTemplateAnswerDTO.name() + "\n" + memberTemplateAnswerDTO.content()
                + GPT_REQUEST_FORMAT_FOR_COMMENT + memberTemplateAnswerDTO.content());

        ObjectMapper om = new ObjectMapper();

        String gptAnswer = getGptAnswer(om, gptRequest);

        log.info("\ngptRequest : {}\ngptAnswer : {} ", gptRequest, gptAnswer);

        return gptAnswer;
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

    private GptRequest createGptRequestWithSystemRole(String content) {
        Message systemMessage = Message.builder()
            .role("system")
            .content(GPT_SYSTEM_ROLE)
            .build();

        Message userMessage = Message.builder()
            .role("user")
            .content(content)
            .build();

        return GptRequest.builder()
            .model("gpt-3.5-turbo")
            .stream(false)
            .messages(List.of(systemMessage, userMessage))
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
}
