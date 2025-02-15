package UMC.career_mate.domain.recruit.controller;

import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.recruit.dto.response.RecommendRecruitsDTO;
import UMC.career_mate.domain.recruit.dto.response.RecruitInfoDTO;
import UMC.career_mate.domain.recruit.enums.JobCode;
import UMC.career_mate.domain.recruit.enums.RecruitSortType;
import UMC.career_mate.domain.recruit.service.RecruitCommandService;
import UMC.career_mate.domain.recruit.service.RecruitQueryService;
import UMC.career_mate.global.annotation.LoginMember;
import UMC.career_mate.global.common.PageResponseDTO;
import UMC.career_mate.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "채용 공고 API", description = "채용 공고 도메인의 API 입니다.")
@RequestMapping("/recruits")
public class RecruitController {

    private final RecruitCommandService recruitCommandService;
    private final RecruitQueryService recruitQueryService;

    @Operation(
        summary = "추천 채용 공고 조회 API",
        description = """
            추천 채용 공고를 조회하는 API입니다.\n\n
            page의 값은 1부터 시작이고, 기본 값은 1입니다.\n\n
            size의 기본값은 6입니다.\n\n
            정렬의 경우\n\n
            전체 (기본 값) -> POSTING_DESC\n\n
            마감 빠른 순 -> DEADLINE_ASC\n\n
            마감 늦은 순 -> DEADLINE_DESC
            """)
    @GetMapping
    public ApiResponse<PageResponseDTO<RecommendRecruitsDTO>> getRecommendRecruitList(
        @RequestParam(defaultValue = "1", required = false) int page,
        @RequestParam(defaultValue = "6", required = false) int size,
        @RequestParam(defaultValue = "POSTING_DESC", required = false) RecruitSortType recruitSortType,
        @LoginMember Member member
    ) {
        return ApiResponse.onSuccess(
            recruitQueryService.getRecommendRecruitList(page, size, recruitSortType, member));
    }

    @Operation(
        summary = "채용 공고 요약 페이지 조회 API",
        description = """
            채용 공고 요약 페이지를 조회하는 API입니다.\n\n
            recruitId : 조회하려는 채용 공고 pk 값
            """)
    @GetMapping("/{recruitId}")
    public ResponseEntity<ApiResponse<RecruitInfoDTO>> getRecruitInfo(@PathVariable Long recruitId,
        @LoginMember Member member) {
        return ResponseEntity.ok(
            ApiResponse.onSuccess(recruitQueryService.findRecruitInfo(member, recruitId)));
    }

    @Deprecated
    @Operation(summary = "채용 공고 db 저장 api", description = "테스트 개발용입니다람쥐")
    @PostMapping
    public ApiResponse<String> saveRecruits(@RequestParam JobCode jobCode) {
        recruitCommandService.saveRecruitInfoOfSaramin(jobCode);
        return ApiResponse.onSuccess("데이터 저장 완료");
    }
}
