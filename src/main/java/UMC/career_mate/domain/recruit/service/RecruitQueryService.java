package UMC.career_mate.domain.recruit.service;

import UMC.career_mate.domain.answer.Answer;
import UMC.career_mate.domain.answer.repository.AnswerRepository;
import UMC.career_mate.domain.chatgpt.GptAnswer;
import UMC.career_mate.domain.chatgpt.dto.request.ChatGPTRequestDTO;
import UMC.career_mate.domain.chatgpt.service.GptAnswerCommandService;
import UMC.career_mate.domain.recruit.dto.FilterConditionDTO;
import UMC.career_mate.domain.chatgpt.repository.GptAnswerRepository;
import UMC.career_mate.domain.chatgpt.service.ChatGptService;
import UMC.career_mate.domain.job.Job;
import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.recruit.Recruit;
import UMC.career_mate.domain.recruit.converter.RecruitConverter;
import UMC.career_mate.domain.recruit.dto.response.RecommendRecruitDTO;
import UMC.career_mate.domain.recruit.dto.response.RecruitInfoDTO;
import UMC.career_mate.domain.recruit.enums.EducationLevel;
import UMC.career_mate.domain.recruit.enums.RecruitKeyword;
import UMC.career_mate.domain.recruit.enums.RecruitSortType;
import UMC.career_mate.domain.recruit.repository.RecruitRepository;
import UMC.career_mate.domain.recruitScrap.repository.RecruitScrapRepository;
import UMC.career_mate.domain.template.enums.TemplateType;
import UMC.career_mate.global.common.PageResponseDTO;
import UMC.career_mate.global.response.exception.GeneralException;
import UMC.career_mate.global.response.exception.code.CommonErrorCode;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitQueryService {

    private final RecruitRepository recruitRepository;
    private final ChatGptService chatGptService;
    private final RecruitScrapRepository recruitScrapRepository;

    private final AnswerRepository answerRepository;

    private final GptAnswerRepository gptAnswerRepository;
    private final GptAnswerCommandService gptAnswerCommandService;

    private final static int ONLY_ENTER_LENGTH = 2;
    private final static int NO_INTERN_EXPERIENCE = 0;
    private final static String PROJECT_PREFIX = "프로젝트 경험 데이터 : ";
    private final static String INTERN_PREFIX = "인턴 경험 데이터 : ";
    private final static String PROJECT_QUESTION_CONTENT_PERIOD = "기간";
    private final static String INTERN_QUESTION_CONTENT_PERIOD = "근무기간";

    public PageResponseDTO<List<RecommendRecruitDTO>> getRecommendRecruitList(int page, int size,
        RecruitSortType recruitSortType, Member member) {
        // GptAnswer 엔티티를 조회한다.
        Optional<GptAnswer> findGptAnswer = gptAnswerRepository.findByMember(member);

        // 인턴 템플릿 답변 데이터와 프로젝트 템플릿 답변 데이터의 질문의 order, sequence 1번을 조회해서 마지막 수정일을 확인한다. -> 최대 2개 조회.
        List<Answer> internAndProjectAnswersFromFirstQuestion = answerRepository.findByMemberAndTemplateTypesAndOrderAndJob(
            member, List.of(TemplateType.INTERN_EXPERIENCE, TemplateType.PROJECT_EXPERIENCE), 1, 1,
            member.getJob());

        FilterConditionDTO filterCondition = createFilterCondition(member, findGptAnswer,
            internAndProjectAnswersFromFirstQuestion);

        // 사용자 프로필의 학력과 상태로 검색에서 사용할 EducationLevel로 치환한다.
        EducationLevel educationLevel = EducationLevel.getEducationLevelFromMemberInfo(
            member.getEducationLevel(), member.getEducationStatus());

        PageRequest pageRequest = PageRequest.of(page - 1, size);

        Page<Recruit> findRecruitPage = recruitRepository.findByKeywordWithBooleanBuilder(
            filterCondition.recruitKeyword(), educationLevel, filterCondition.careerYear(),
            recruitSortType, pageRequest);

        return createPageResponseDTO(page, size, findRecruitPage, member);
    }

    public RecruitInfoDTO findRecruitInfo(Member member, Long recruitId) {
        Recruit recruit = recruitRepository.findById(recruitId)
            .orElseThrow(() -> new GeneralException(CommonErrorCode.NOT_FOUND_RECRUIT));

        // TODO: db에서 사용자의 템플릿 데이터 가져오는걸로 수정
        ChatGPTRequestDTO chatGPTRequestDTO = createDummyData(member);

        String comment = chatGptService.getComment(chatGPTRequestDTO);

        return RecruitConverter.toRecruitInfoDTO(comment, recruit);
    }

    private FilterConditionDTO createFilterCondition(Member member,
        Optional<GptAnswer> findGptAnswer,
        List<Answer> internAndProjectAnswersFromFirstQuestion) {

        int careerYear;
        RecruitKeyword recruitKeyword;
        if (findGptAnswer.isEmpty()) { // GptAnswer이 없는 경우 (Gpt 요청 한번도 안한 경우)
            log.info("GptAnswer가 없는 유저인 상황, GptAnswer의 insert가 발생한다");

            // 인턴 템플릿 근무기간 답변 데이터에 한해서 경력을 계산한다.
            careerYear = calculateCareerYear(member);

            // 인턴 + 프로젝트 템플릿 답변 데이터에서 기간을 제외한 데이터로 추천 직무를 가져온다.
            recruitKeyword = calculateRecruitKeyword(member);

            gptAnswerCommandService.saveGptAnswer(member, recruitKeyword, careerYear);
        } else {
            GptAnswer gptAnswer = findGptAnswer.get();

            log.info("internAndProjectAnswersFromFirstQuestion의 크기 : {}", internAndProjectAnswersFromFirstQuestion.size());

            List<Answer> updatedAnswerList = internAndProjectAnswersFromFirstQuestion.stream()
                .filter(answer -> answer.getUpdatedAt().isAfter(gptAnswer.getUpdatedAt()))
                .toList();

            // GptAnswer 마지막 요청 이후에 프로젝트 or 인턴 템플릿 답변이 수정된 경우
            if (!updatedAnswerList.isEmpty()) {
                log.info("마지막 Gpt 요청 이후 인턴 or 프로젝트 답변 수정한 상황, GptAnswer의 업데이트가 발생한다.");

                // 인턴 템플릿 근무기간 답변 데이터에 한해서 경력을 계산한다.
                careerYear = calculateCareerYear(member);

                // 인턴 + 프로젝트 템플릿 답변 데이터에서 기간을 제외한 데이터로 추천 직무를 가져온다.
                recruitKeyword = calculateRecruitKeyword(member);

                gptAnswerCommandService.updateGptAnswer(gptAnswer, recruitKeyword, careerYear);
            } else {
                log.info("마지막 Gpt 요청 이후 인턴 or 프로젝트 답변 수정한 적이 없는 상황, GptAnswer에 대해 아무 일도 일어나지 않는다.");
                careerYear = gptAnswer.getCareerYear();
                recruitKeyword = gptAnswer.getRecruitKeyword();
            }
        }

        return RecruitConverter.toFilterConditionDTO(recruitKeyword, careerYear);
    }

    private int calculateCareerYear(Member member) {
        Job job = member.getJob();

        // 인턴 템플릿의 근무기간 답변 데이터를 가져온다. 0개 or 2개
        List<Answer> periodAnswerList = answerRepository.findAnswersByMemberAndQuestionContentAndTemplateTypeAndJob(
            member, INTERN_QUESTION_CONTENT_PERIOD, TemplateType.INTERN_EXPERIENCE, job);

        // 아직 답변을 안한 상태 (템플릿 답변 작성 없이 바로 공고 조회를 한 경우) -> 경력 0년 반환
        if (periodAnswerList.isEmpty()) {
            return NO_INTERN_EXPERIENCE;
        }

        StringBuilder contentBuilder = new StringBuilder();

        periodAnswerList.forEach(
            answer -> contentBuilder.append(answer.getContent() + "\n")
        );

        // 인턴 근무 기간이 빈 문자열인 경우 -> 경력 0년 반환
        if (contentBuilder.length() == ONLY_ENTER_LENGTH) {
            log.info("인턴 근무 기간이 빈 문자열인 경우 -> 경력 0년 반환");
            return NO_INTERN_EXPERIENCE;
        }

        // 근무 기간이 입력되어 있는 경우 -> gpt에게 경력 계산 요청
        return chatGptService.getCareerYear(contentBuilder.toString());
    }

    private RecruitKeyword calculateRecruitKeyword(Member member) {
        Job job = member.getJob();

        // 프로젝트 템플릿의 답변, 인턴 템플릿의 답변을 가져온다.
        Map<TemplateType, List<Answer>> templateTypeByAnswers = getTemplateTypeByAnswers(
            member, job);

        List<Answer> projectAnswers = templateTypeByAnswers.getOrDefault(
            TemplateType.PROJECT_EXPERIENCE, List.of());
        List<Answer> internAnswers = templateTypeByAnswers.getOrDefault(
            TemplateType.INTERN_EXPERIENCE, List.of());

        // (프로젝트, 인턴) 템플릿 답변 없이, 바로 채용 공고 조회하는 경우 -> answer 생성 안된 경우
        if (projectAnswers.isEmpty() && internAnswers.isEmpty()) {
            return RecruitKeyword.getRecruitKeywordFromProfileJob(job);
        }

        StringBuilder contentBuilder = new StringBuilder();
        int projectEnterCnt = createChatGptRequestContent(contentBuilder, projectAnswers,
            PROJECT_PREFIX, PROJECT_QUESTION_CONTENT_PERIOD);
        int internEnterCnt = createChatGptRequestContent(contentBuilder, internAnswers,
            INTERN_PREFIX, INTERN_QUESTION_CONTENT_PERIOD);

        // 최종 데이터가 기본 틀을 제외한 빈 문자열인 경우
        if (isEmptyContentBuilder(contentBuilder, projectEnterCnt, internEnterCnt)) {
            log.info("최종 데이터가 기본 틀 데이터를 제외하고 빈 문자열인 경우");
            return RecruitKeyword.getRecruitKeywordFromProfileJob(job);
        }

        RecruitKeyword recruitKeyword = chatGptService.getRecruitKeyword(contentBuilder.toString());

        // chatgpt 응답이 똑바로 오지 않은 경우, 기존 프로필 job으로 대체
        if (Objects.isNull(recruitKeyword)) {
            log.info("gpt 답변이 RecruitKeyword에 없는 값이라서 null인 경우");
            return RecruitKeyword.getRecruitKeywordFromProfileJob(job);
        }

        return recruitKeyword;
    }

    private Map<TemplateType, List<Answer>> getTemplateTypeByAnswers(Member member, Job job) {
        List<Answer> findAnswers = answerRepository.findByMemberAndTemplateTypesAndJob(member,
            List.of(TemplateType.PROJECT_EXPERIENCE, TemplateType.INTERN_EXPERIENCE), job);

        return findAnswers.stream()
            .collect(Collectors.groupingBy(
                answer -> answer.getQuestion().getTemplate().getTemplateType()));
    }

    // 답변 리스트를 StringBuilder에 추가하고, 추가된 개수를 반환
    private int createChatGptRequestContent(StringBuilder contentBuilder, List<Answer> answers,
        String prefix,
        String excludeContent) {
        contentBuilder.append(prefix);
        int enterCnt = 0;
        for (Answer answer : answers) {
            if (!answer.getQuestion().getContent().equals(excludeContent)) {
                contentBuilder.append(answer.getContent().trim()).append("\n");
                enterCnt++;
            }
        }
        return enterCnt;
    }

    // StringBuilder가 기본 틀 외에 내용이 없는지 확인
    private boolean isEmptyContentBuilder(StringBuilder contentBuilder, int projectEnterCnt,
        int internEnterCnt) {
        return contentBuilder.length()
            == PROJECT_PREFIX.length() + projectEnterCnt + INTERN_PREFIX.length() + internEnterCnt;
    }

    private PageResponseDTO<List<RecommendRecruitDTO>> createPageResponseDTO(int page,
        int size, Page<Recruit> findRecruitPage, Member member) {
        Set<Long> scrapedRecruitIds = recruitScrapRepository.findRecruitIdsByMember(member);

        boolean hasNext = findRecruitPage.getSize() == size + 1;

        List<RecommendRecruitDTO> recommendRecruitDTOList = findRecruitPage.stream()
            .limit(size)
            .map(recruit -> RecruitConverter.toRecommendRecruitDTO(recruit,
                scrapedRecruitIds.contains(recruit.getId())))
            .toList();

        return PageResponseDTO.<List<RecommendRecruitDTO>>builder()
            .page(page)
            .hasNext(hasNext)
            .result(recommendRecruitDTOList)
            .build();
    }

    private ChatGPTRequestDTO createDummyData(Member member) {
        return ChatGPTRequestDTO.builder()
            .name(member.getName())
            .content(
                "네이버 / 백엔드 개발팀, 백엔드 개발자, 2023.06.01~2023.12.01, Spring 대규모 데이터 처리 및 최적화 과정을 통해 성능 개선의 중요성을 배웠습니다., 사용자 데이터를 기반으로 한 개인화 서비스 개발에 참여하며, 데이터의 가치를 깊이 이해할 수 있었습니다., 대규모 시스템 아키텍처 설계 경험을 통해 기술적인 자신감이 크게 향상되었습니다.")
            .build();
    }
}
