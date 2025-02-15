package UMC.career_mate.domain.recruit.service;

import UMC.career_mate.domain.answer.Answer;
import UMC.career_mate.domain.answer.repository.AnswerRepository;
import UMC.career_mate.domain.chatgpt.GptAnswer;
import UMC.career_mate.domain.recruit.dto.MemberTemplateAnswerDTO;
import UMC.career_mate.domain.chatgpt.service.GptAnswerCommandService;
import UMC.career_mate.domain.recruit.dto.FilterConditionDTO;
import UMC.career_mate.domain.chatgpt.repository.GptAnswerRepository;
import UMC.career_mate.domain.chatgpt.service.ChatGptService;
import UMC.career_mate.domain.job.Job;
import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.recruit.Recruit;
import UMC.career_mate.domain.recruit.converter.RecruitConverter;
import UMC.career_mate.domain.recruit.dto.response.RecommendRecruitsDTO;
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
    private final static String TEMPLATE_ANSWER_IS_NULL_OR_EMPTY_COMMENT = "인턴 또는 프로젝트 템플릿에 대해 답변을 작성하고," +  "\n" + "Chat GPT의 코멘트를 받아보세요!";

    public PageResponseDTO<RecommendRecruitsDTO> getRecommendRecruitList(int page, int size,
        RecruitSortType recruitSortType, Member member) {
        FilterConditionDTO filterCondition = createFilterCondition(member);

        // 사용자 프로필의 학력과 상태로 검색에서 사용할 EducationLevel로 치환한다.
        EducationLevel educationLevel = EducationLevel.getEducationLevelFromMemberInfo(
            member.getEducationLevel(), member.getEducationStatus());

        PageRequest pageRequest = PageRequest.of(page - 1, size);

        Page<Recruit> findRecruitPage = recruitRepository.findByKeywordWithBooleanBuilder(
            filterCondition.recruitKeyword(), educationLevel, filterCondition.careerYear(),
            recruitSortType, pageRequest);

        return createPageResponseDTO(findRecruitPage, member);
    }

    public RecruitInfoDTO findRecruitInfo(Member member, Long recruitId) {
        Recruit recruit = recruitRepository.findById(recruitId)
            .orElseThrow(() -> new GeneralException(CommonErrorCode.NOT_FOUND_RECRUIT));

        String comment = createComment(member);

        return RecruitConverter.toRecruitInfoDTO(comment, recruit);
    }

    private FilterConditionDTO createFilterCondition(Member member) {
        Optional<GptAnswer> findGptAnswer = gptAnswerRepository.findByMember(member);

        // 질문의 order 1번이고 인턴 템플릿 답변 데이터와 프로젝트 템플릿 답변 데이터의 1번을 조회 -> 최대 2개 조회. (updated_at 확인)
        List<Answer> internAndProjectAnswersFromFirstQuestion = answerRepository.findByMemberAndTemplateTypesAndOrderAndJob(
            member, List.of(TemplateType.INTERN_EXPERIENCE, TemplateType.PROJECT_EXPERIENCE), 1, 1,
            member.getJob());

        int careerYear;
        RecruitKeyword recruitKeyword;
        if (findGptAnswer.isEmpty()) { // GptAnswer이 없는 경우
            log.info("GptAnswer가 없는 유저인 상황, GptAnswer에 대해 insert가 발생한다");

            // 인턴 템플릿 근무기간 답변 데이터에 한해서 경력을 계산한다.
            careerYear = calculateCareerYear(member);

            // 인턴 + 프로젝트 템플릿 답변 데이터에서 기간을 제외한 데이터로 추천 직무를 가져온다.
            recruitKeyword = calculateRecruitKeyword(member);

            gptAnswerCommandService.saveGptAnswer(member, recruitKeyword, careerYear);
        } else {
            GptAnswer gptAnswer = findGptAnswer.get();

            log.info("internAndProjectAnswersFromFirstQuestion 크기 : {}",
                internAndProjectAnswersFromFirstQuestion.size());

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

        if (periodAnswerList.isEmpty()) {
            log.info("아직 답변을 안한 상태 (템플릿 답변 작성 없이 바로 공고 조회를 한 경우) -> 경력 0년 반환");
            return NO_INTERN_EXPERIENCE;
        }

        StringBuilder contentBuilder = new StringBuilder();

        periodAnswerList.forEach(
            answer -> contentBuilder.append(answer.getContent() + "\n")
        );

        if (contentBuilder.length() == ONLY_ENTER_LENGTH) {
            log.info("인턴 근무 기간이 빈 문자열인 경우 -> 경력 0년 반환");
            return NO_INTERN_EXPERIENCE;
        }

        // 근무 기간이 입력되어 있는 경우 -> gpt에게 경력 계산 요청
        return chatGptService.getCareerYear(contentBuilder.toString());
    }

    private RecruitKeyword calculateRecruitKeyword(Member member) {
        Job job = member.getJob();

        // pm, 디자이너는 gpt 요청 x (상세 직무 나눌 필요 없음)
        if (job.getName().equals("PM(Product/Project Manager)") || job.getName().equals("Designer")) {
            return RecruitKeyword.getRecruitKeywordFromProfileJob(job);
        }

        // 프로젝트 템플릿의 답변, 인턴 템플릿의 답변을 가져온다.
        Map<TemplateType, List<Answer>> templateTypeByAnswers = getTemplateTypeByAnswers(
            member, job);

        List<Answer> projectAnswers = templateTypeByAnswers.getOrDefault(
            TemplateType.PROJECT_EXPERIENCE, List.of());
        List<Answer> internAnswers = templateTypeByAnswers.getOrDefault(
            TemplateType.INTERN_EXPERIENCE, List.of());

        if (projectAnswers.isEmpty() && internAnswers.isEmpty()) {
            log.info("(프로젝트, 인턴) 템플릿 답변 없이, 바로 채용 공고 조회하는 경우 -> answer 생성 안된 경우");
            return RecruitKeyword.getRecruitKeywordFromProfileJob(job);
        }

        StringBuilder contentBuilder = new StringBuilder();
        int projectEnterCnt = createChatGptRequestContent(contentBuilder, projectAnswers,
            PROJECT_PREFIX, PROJECT_QUESTION_CONTENT_PERIOD);
        int internEnterCnt = createChatGptRequestContent(contentBuilder, internAnswers,
            INTERN_PREFIX, INTERN_QUESTION_CONTENT_PERIOD);

        if (isEmptyContentBuilder(contentBuilder, projectEnterCnt, internEnterCnt)) {
            log.info("최종 데이터가 기본 틀 데이터를 제외하고 빈 문자열인 경우");
            return RecruitKeyword.getRecruitKeywordFromProfileJob(job);
        }

        RecruitKeyword recruitKeyword = chatGptService.getRecruitKeyword(contentBuilder.toString(), member.getJob());

        if (Objects.isNull(recruitKeyword)) {
            log.info("gpt 답변이 RecruitKeyword에 없는 값이라서 null인 경우 -> 멤버 프로필 job으로 대체");
            return RecruitKeyword.getRecruitKeywordFromProfileJob(job);
        }

        return recruitKeyword;
    }

    private String createComment(Member member) {
        Job job = member.getJob();

        Map<TemplateType, List<Answer>> templateTypeByAnswers = getTemplateTypeByAnswers(member,
            job);

        List<Answer> projectAnswers = templateTypeByAnswers.getOrDefault(
            TemplateType.PROJECT_EXPERIENCE, List.of());
        List<Answer> internAnswers = templateTypeByAnswers.getOrDefault(
            TemplateType.INTERN_EXPERIENCE, List.of());

        if (projectAnswers.isEmpty() && internAnswers.isEmpty()) {
            log.info("(프로젝트, 인턴) 템플릿 답변 없이, 채용 공고 요약 페이지 조회하는 경우 -> answer 생성 안된 경우");
            return TEMPLATE_ANSWER_IS_NULL_OR_EMPTY_COMMENT;
        }

        StringBuilder contentBuilder = new StringBuilder();
        int projectEnterCnt = createChatGptRequestContent(contentBuilder, projectAnswers,
            PROJECT_PREFIX, PROJECT_QUESTION_CONTENT_PERIOD);
        int internEnterCnt = createChatGptRequestContent(contentBuilder, internAnswers,
            INTERN_PREFIX, INTERN_QUESTION_CONTENT_PERIOD);

        if (isEmptyContentBuilder(contentBuilder, projectEnterCnt, internEnterCnt)) {
            log.info("최종 데이터가 기본 틀 데이터를 제외하고 빈 문자열인 경우");
            return TEMPLATE_ANSWER_IS_NULL_OR_EMPTY_COMMENT;
        }

        MemberTemplateAnswerDTO memberTemplateAnswerDTO = RecruitConverter.toMemberTemplateAnswerDTO(
            member, contentBuilder.toString());

        return chatGptService.getComment(memberTemplateAnswerDTO);
    }

    private Map<TemplateType, List<Answer>> getTemplateTypeByAnswers(Member member, Job job) {
        List<Answer> findAnswers = answerRepository.findByMemberAndTemplateTypesAndJob(member,
            List.of(TemplateType.PROJECT_EXPERIENCE, TemplateType.INTERN_EXPERIENCE), job);

        return findAnswers.stream()
            .collect(Collectors.groupingBy(
                answer -> answer.getQuestion().getTemplate().getTemplateType()));
    }

    // 기간 데이터를 제외하고 답변 리스트의 content를 StringBuilder에 추가, 추가된 개수를 반환
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

    private PageResponseDTO<RecommendRecruitsDTO> createPageResponseDTO(Page<Recruit> findRecruitPage, Member member) {
        Set<Long> scrappedRecruitIds = recruitScrapRepository.findRecruitIdsByMember(member);

        RecommendRecruitsDTO recommendRecruitsDTO = RecruitConverter.toRecommendRecruitsDTO(member,
            findRecruitPage.stream()
                .map(recruit -> RecruitConverter.toRecruitThumbNailInfoDTO(recruit,
                    scrappedRecruitIds.contains(recruit.getId())))
                .toList());

        return PageResponseDTO.<RecommendRecruitsDTO>builder()
            .page(findRecruitPage.getNumber() + 1)
            .totalPages(findRecruitPage.getTotalPages())
            .result(recommendRecruitsDTO)
            .build();
    }
}
