package UMC.career_mate.domain.template.controller;

import UMC.career_mate.domain.template.dto.response.TemplateResponseDTO;
import UMC.career_mate.domain.template.enums.TemplateType;
import UMC.career_mate.domain.template.service.TemplateQueryService;
import UMC.career_mate.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static UMC.career_mate.global.response.result.code.CommonResultCode.GET_TEMPLATE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/templates")
public class TemplateController {
    private final TemplateQueryService templateQueryService;

    @GetMapping
    @Operation(
            summary = "직무에 맞는 템플릿 조회 API",
            description =
                    """
                    주어진 직무 ID와 템플릿 타입에 맞는 템플릿을 반환하는 API 입니다. \
                    템플릿 타입은 다음과 같습니다.\s
                    1. 인턴 경험 (INTERN_EXPERIENCE)\s
                    2. 프로젝트 경험 (PROJECT_EXPERIENCE)\s
                    3. 기타 활동 (OTHER_ACTIVITIES)\s
                    4. 보유 기술 (TECHNICAL_SKILLS)\s
                    5. 최종 정리 (SUMMARY)
                    """
    )
    public ApiResponse<TemplateResponseDTO> getTemplate(@RequestParam Long jobId, @RequestParam TemplateType type) {
        return ApiResponse.onSuccess(GET_TEMPLATE, templateQueryService.getTemplate(jobId, type));
    }
}
