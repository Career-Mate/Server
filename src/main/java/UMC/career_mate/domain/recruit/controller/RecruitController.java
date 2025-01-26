package UMC.career_mate.domain.recruit.controller;

import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.recruit.dto.response.RecommendRecruitDTO;
import UMC.career_mate.domain.recruit.dto.response.RecruitInfoDTO;
import UMC.career_mate.domain.recruit.enums.JobCode;
import UMC.career_mate.domain.recruit.enums.RecruitSortType;
import UMC.career_mate.domain.recruit.service.RecruitCommandService;
import UMC.career_mate.domain.recruit.service.RecruitQueryService;
import UMC.career_mate.global.annotation.LoginMember;
import UMC.career_mate.global.common.PageResponseDTO;
import UMC.career_mate.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Operation(summary = "추천 채용 공고 조회 API", description = "추천 채용 공고를 조회하는 API입니다.")
    @GetMapping
    public ApiResponse<PageResponseDTO<List<RecommendRecruitDTO>>> getRecommendRecruitList(
        @RequestParam(defaultValue = "1", required = false) int page,
        @RequestParam(defaultValue = "6", required = false) int size,
        @RequestParam(defaultValue = "POSTING_DESC", required = false) RecruitSortType recruitSortType,
        @LoginMember Member member
        ) {
        return ApiResponse.onSuccess(
            recruitQueryService.getRecommendRecruitList(page, size, recruitSortType, member));
    }

    @Operation(summary = "채용 공고 요약 페이지 조회 API", description = "채용 공고 요약 페이지를 조회하는 API입니다.")
    @GetMapping("/{recruitId}")
    public ResponseEntity<ApiResponse<RecruitInfoDTO>> getRecruitInfo(@PathVariable Long recruitId, @LoginMember Member member) {
        return ResponseEntity.ok(
            ApiResponse.onSuccess(recruitQueryService.findRecruitInfo(member, recruitId)));
    }

    @Deprecated
    @Operation(summary = "채용 공고 db 저장 api", description = "테스트 개발용입니다")
    @PostMapping
    public ApiResponse<String> saveRecruits(@RequestParam JobCode jobCode) {
        recruitCommandService.saveRecruitInfoOfSaramin(jobCode);
        return ApiResponse.onSuccess("데이터 저장 완료");
    }
}
