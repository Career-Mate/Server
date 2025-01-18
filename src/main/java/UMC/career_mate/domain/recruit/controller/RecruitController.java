package UMC.career_mate.domain.recruit.controller;

import UMC.career_mate.domain.recruit.enums.JobCode;
import UMC.career_mate.domain.recruit.service.RecruitCommandService;
import UMC.career_mate.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recruits")
public class RecruitController {

    private final RecruitCommandService recruitCommandService;

    @Deprecated
    @Operation(summary = "채용 공고 db 저장 api", description = "테스트 개발용입니다")
    @PostMapping
    public ApiResponse<String> saveRecruits(@RequestParam JobCode jobCode) {
        recruitCommandService.saveRecruitInfoOfSaramin(jobCode);
        return ApiResponse.onSuccess("데이터 저장 완료");
    }
}
