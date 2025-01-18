package UMC.career_mate.domain.planner.controller;

import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.planner.dto.request.CreatePlannerDTO;
import UMC.career_mate.domain.planner.service.PlannerCommandService;
import UMC.career_mate.global.annotation.LoginMember;
import UMC.career_mate.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/planner")
public class PlannerController {

    private final PlannerCommandService plannerCommandService;

    @PostMapping
    @Operation(
            summary = "플래너 생성",
            description = """
                    새로운 플래너를 생성합니다.
                    값이 비어있어도 상관 없습니다.
                    ### Example JSON:
                    ```json
                    {
                        "activityName": "React.js로 커리어 메이트 00파트 개발",
                        "startTime": "2025-12-23T09:00:00",
                        "endTime": "2025-12-23T18:00:00",
                        "specific": "React.js를 활용한 프로젝트 5개 기능 구현",
                        "measurable": "개인 프로젝트를 GitHub에 배포 + 최소 5개 기능 구현",
                        "achievable": "하루에 최소 3시간 이상 투자하기",
                        "relevant": "프로젝트 경험을 통한 기술 및 협업 역량 향상 기대",
                        "timeBound": "2025년 2월 10일 - 12/23~1/10: 00부분까지 완료, 1/12~1/26: API 연결",
                        "otherPlans": "매주 토요일 강남역 대면 미팅 참석"
                    }
                    ```
                    """)

    public ApiResponse<String> savePlanner(@LoginMember Member member, @RequestBody CreatePlannerDTO createPlannerDTO) {
        plannerCommandService.savePlanner(member, createPlannerDTO);
        return ApiResponse.onSuccess("플래너 추가 완료");
    }

    @PatchMapping
    @Operation(
            summary = "플래너 수정",
            description = """
                    플래너를 수정합니다.
                    값이 비어있어도 상관 없습니다.
                    ### Example JSON:
                    ```json
                    {
                        "activityName": "React.js로 커리어 메이트 00파트 개발",
                        "startTime": "2025-12-23T09:00:00",
                        "endTime": "2025-12-23T18:00:00",
                        "specific": "React.js를 활용한 프로젝트 5개 기능 구현",
                        "measurable": "개인 프로젝트를 GitHub에 배포 + 최소 5개 기능 구현",
                        "achievable": "하루에 최소 3시간 이상 투자하기",
                        "relevant": "프로젝트 경험을 통한 기술 및 협업 역량 향상 기대",
                        "timeBound": "2025년 2월 10일 - 12/23~1/10: 00부분까지 완료, 1/12~1/26: API 연결",
                        "otherPlans": "매주 토요일 강남역 대면 미팅 참석"
                    }
                    ```
                    """)

    public ApiResponse<String> editPlanner(@LoginMember Member member, @RequestBody CreatePlannerDTO createPlannerDTO) {
        plannerCommandService.editPlanner(member, createPlannerDTO);
        return ApiResponse.onSuccess("플래너 수정 완료");
    }
}
