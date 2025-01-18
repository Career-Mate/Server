package UMC.career_mate.domain.recruit.service;

import UMC.career_mate.domain.recruit.Recruit;
import UMC.career_mate.domain.recruit.converter.RecruitConverter;
import UMC.career_mate.domain.recruit.dto.api.SaraminResponseDTO;
import UMC.career_mate.domain.recruit.dto.api.SaraminResponseDTO.Job;
import UMC.career_mate.domain.recruit.dto.api.SaraminResponseDTO.Jobs;
import UMC.career_mate.domain.recruit.enums.JobCode;
import UMC.career_mate.domain.recruit.repository.RecruitRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RecruitCommandService {

    private final RecruitRepository recruitRepository;

    @Value("${saramin.access-key}")
    private String accessKey;
    private static final int PAGE_SIZE = 110;

    public void saveRecruitInfoOfSaramin(JobCode jobCode) {
        Set<String> recruitUrlSet = recruitRepository.findRecruitUrls();

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper om = new ObjectMapper();
        HttpEntity httpEntity = createHttpEntity();

        int pageNum = 0;
        int totalDataCnt = 1;
        while (totalDataCnt - pageNum * PAGE_SIZE > 0) {
            log.info("현재 페이지: {}", pageNum);

            URI uri = createUri(jobCode, pageNum);
            String saraminApiResponse = requestToSaraminApi(restTemplate, uri, httpEntity);

            SaraminResponseDTO saraminResponseDTO = null;
            try {
                saraminResponseDTO = om.readValue(saraminApiResponse, SaraminResponseDTO.class);
            } catch (JsonProcessingException e) {
                log.error("역직렬화 중 에러 발생");
                throw new RuntimeException(e);
            }

            Jobs jobs = saraminResponseDTO.jobs();

            if (pageNum == 0) {
                totalDataCnt = jobs.total();
            }

            List<Job> jobList = jobs.job();

            for (Job job : jobList) {
                String recruitUrl = job.url();
                if (recruitUrl.contains("&")) {
                    int subsIndex = recruitUrl.indexOf("&");
                    recruitUrl = recruitUrl.substring(0, subsIndex);
                }

                if (recruitUrlSet.contains(recruitUrl)) {
                    log.info("이미 존재하는 채용 공고 데이터입니다. 공고 url : {}", recruitUrl);
                    continue;
                }

                String companyInfoUrl = job.company().detail().href(); // null인 경우 존재

                if (Objects.nonNull(companyInfoUrl) && companyInfoUrl.contains("&")) {
                    int subsIndex = companyInfoUrl.indexOf("&");
                    companyInfoUrl = companyInfoUrl.substring(0 ,subsIndex);
                }

                Recruit recruit = RecruitConverter.toRecruit(job, recruitUrl, companyInfoUrl);
                recruitRepository.save(recruit);
            }

            pageNum++;
        }

        log.info("사람인 api 호출 종료");
        log.info("{} 직무 코드 요청에서 저장된 총 데이터 : {}", jobCode.getName(), totalDataCnt);
    }

    private HttpEntity createHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

    private URI createUri(JobCode jobCode, int pageNum) {
        DefaultUriBuilderFactory builder = new DefaultUriBuilderFactory();
        String uriString = builder.builder()
            .scheme("https")
            .host("oapi.saramin.co.kr")
            .path("/job-search")
            .queryParam("access-key", accessKey)
            .queryParam("job_cd", jobCode.getCode())
            .queryParam("start", pageNum)
            .queryParam("count", PAGE_SIZE)
            .build()
            .toString();

        URI uri = URI.create(uriString);
        return uri;
    }

    private String requestToSaraminApi(RestTemplate restTemplate, URI uri, HttpEntity entity) {
        ResponseEntity<String> response = restTemplate.exchange(
            uri,
            HttpMethod.GET,
            entity,
            String.class
        );
        String body = response.getBody();
        log.info(body);
        return body;
    }
}
