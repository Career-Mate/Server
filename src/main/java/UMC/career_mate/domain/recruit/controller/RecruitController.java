package UMC.career_mate.domain.recruit.controller;

import UMC.career_mate.domain.recruit.dto.response.RecommendRecruitDTO;
import UMC.career_mate.domain.recruit.enums.EducationLevel;
import UMC.career_mate.domain.recruit.enums.JobCode;
import UMC.career_mate.domain.recruit.enums.RecruitKeyword;
import UMC.career_mate.domain.recruit.enums.RecruitSortType;
import UMC.career_mate.domain.recruit.service.RecruitCommandService;
import UMC.career_mate.domain.recruit.service.RecruitQueryService;
import UMC.career_mate.global.common.PageResponseDto;
import UMC.career_mate.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recruits")
public class RecruitController {

    private final RecruitCommandService recruitCommandService;
    private final RecruitQueryService recruitQueryService;

    @Deprecated
    @Operation(summary = "채용 공고 db 저장 api", description = "테스트 개발용입니다")
    @PostMapping
    public ApiResponse<String> saveRecruits(@RequestParam JobCode jobCode) {
        recruitCommandService.saveRecruitInfoOfSaramin(jobCode);
        return ApiResponse.onSuccess("데이터 저장 완료");
    }

    /**
     * Member 완성되면 Member 사용하고, page, size 제외 파라미터 전부 삭제 예정
     */
    @Operation(summary = "추천 채용 공고 조회 API", description = "추천 채용 공고를 조회하는 API입니다.")
    @GetMapping
    public ApiResponse<PageResponseDto<List<RecommendRecruitDTO>>> getRecommendRecruitList(
        @RequestParam(defaultValue = "1", required = false) int page,
        @RequestParam(defaultValue = "6", required = false) int size,
        @RequestParam RecruitKeyword recruitKeyword,
        @RequestParam EducationLevel educationLevel,
        @RequestParam Integer careerYear,
        @RequestParam RecruitSortType recruitSortType
    ) {
        return ApiResponse.onSuccess(
            recruitQueryService.getRecommendRecruitList(page, size, recruitKeyword,
                educationLevel, careerYear, recruitSortType));
    }
}
