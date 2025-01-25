package UMC.career_mate.domain.recruit.service;

import UMC.career_mate.domain.chatgpt.dto.request.ChatGPTRequestDTO;
import UMC.career_mate.domain.chatgpt.dto.response.FilterConditionDTO;
import UMC.career_mate.domain.chatgpt.service.ChatGptService;
import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.recruit.Recruit;
import UMC.career_mate.domain.recruit.converter.RecruitConverter;
import UMC.career_mate.domain.recruit.dto.response.RecommendRecruitDTO;
import UMC.career_mate.domain.recruit.dto.response.RecruitInfoDTO;
import UMC.career_mate.domain.recruit.enums.EducationLevel;
import UMC.career_mate.domain.recruit.enums.RecruitSortType;
import UMC.career_mate.domain.recruit.repository.RecruitRepository;
import UMC.career_mate.global.common.PageResponseDTO;
import UMC.career_mate.global.response.exception.GeneralException;
import UMC.career_mate.global.response.exception.code.CommonErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitQueryService {

    private final RecruitRepository recruitRepository;
    private final ChatGptService chatGptService;

    public PageResponseDTO<List<RecommendRecruitDTO>> getRecommendRecruitList(int page, int size,
        RecruitSortType recruitSortType, Member member) {

        // TODO: db에서 사용자의 템플릿 데이터 가져오는걸로 수정
        // TODO: 템플릿 수정일과 gpt 요청일 비교 후 수정일이 더 최근인 경우 gpt에 템플릿 분석 요청, 아니면 저장해둔 경력 데이터 재사용 로직 추가
        ChatGPTRequestDTO chatGPTRequestDTO = createDummyData(member);

        FilterConditionDTO filterCondition = chatGptService.getFilterCondition(chatGPTRequestDTO);

        EducationLevel educationLevel = EducationLevel.getEducationLevelFromMemberInfo(
            member.getEducationLevel(), member.getEducationStatus());

        PageRequest pageRequest = PageRequest.of(page - 1, size);

        Page<Recruit> findRecruitPage = recruitRepository.findByKeywordWithBooleanBuilder(
            filterCondition.recruitKeyword(), educationLevel, filterCondition.careerYear(),
            recruitSortType, pageRequest);

        return createPageResponseDTO(page, size, findRecruitPage);
    }

    public RecruitInfoDTO findRecruitInfo(Member member, Long recruitId) {
        Recruit recruit = recruitRepository.findById(recruitId)
            .orElseThrow(() -> new GeneralException(CommonErrorCode.NOT_FOUND_RECRUIT));

        // TODO: db에서 사용자의 템플릿 데이터 가져오는걸로 수정
        ChatGPTRequestDTO chatGPTRequestDTO = createDummyData(member);

        String comment = chatGptService.getComment(chatGPTRequestDTO);

        return RecruitConverter.toRecruitInfoDTO(comment, recruit);
    }

    private PageResponseDTO<List<RecommendRecruitDTO>> createPageResponseDTO(int page,
        int size, Page<Recruit> findRecruitPage) {
        boolean hasNext = findRecruitPage.getSize() == size + 1;

        List<RecommendRecruitDTO> recommendRecruitDTOList = findRecruitPage.stream()
            .limit(size)
            .map(recruit -> RecruitConverter.toRecommendRecruitDTO(recruit))
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
