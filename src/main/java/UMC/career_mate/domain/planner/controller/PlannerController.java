package UMC.career_mate.domain.planner.controller;

import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.planner.dto.request.CreatePlannerListDTO;
import UMC.career_mate.domain.planner.dto.response.PlannerListResponseDTO;
import UMC.career_mate.domain.planner.service.PlannerCommandService;
import UMC.career_mate.domain.planner.service.PlannerQueryService;
import UMC.career_mate.global.annotation.LoginMember;
import UMC.career_mate.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/planner")
public class PlannerController {

    private final PlannerCommandService plannerCommandService;
    private final PlannerQueryService plannerQueryService;

    @PostMapping
    @Operation(
            summary = "플래너 생성(TEST용)",
            description = """
                    [프로필 작성 완료시 플래너가 자동 생성되기 때문에, PATCH API를 사용해주시면 되겠습니다]
                    요청한 멤버에 해당하는 빈 플래너를 생성합니다.
                    ```
                    """)

    public ApiResponse<String> initPlanner(@LoginMember Member member) {
        plannerCommandService.initPlanner(member);
        return ApiResponse.onSuccess("플래너 생성 완료");
    }

    @PatchMapping
    @Operation(
            summary = "플래너 수정",
            description = """
                    [프로필 작성 완료시 플래너가 자동 생성되기 때문에, 해당 API를 사용해주시면 되겠습니다]
                    플래너를 수정합니다.
                    값이 비어있어도 상관 없습니다.
                    ### Example JSON:
                    ```json
                    planners : [
                        {
                            "activityName": "React.js로 커리어 메이트 00파트 개발",
                            "startTime": "2025-12-23T09:00:00",
                            "endTime": "2025-12-23T18:00:00",
                            "specifics": "React.js를 활용한 프로젝트 5개 기능 구현",
                            "measurable": "개인 프로젝트를 GitHub에 배포 + 최소 5개 기능 구현",
                            "achievable": "하루에 최소 3시간 이상 투자하기",
                            "relevant": "프로젝트 경험을 통한 기술 및 협업 역량 향상 기대",
                            "timeBound": "2025년 2월 10일 - 12/23~1/10: 00부분까지 완료, 1/12~1/26: API 연결",
                            "otherPlans": "매주 토요일 강남역 대면 미팅 참석"
                        },
                        {
                            "activityName": "React.js로 커리어 메이트 00파트 개발",
                            "startTime": "2025-12-23T09:00:00",
                            "endTime": "2025-12-23T18:00:00",
                            "specifics": "React.js를 활용한 프로젝트 5개 기능 구현",
                            "measurable": "개인 프로젝트를 GitHub에 배포 + 최소 5개 기능 구현",
                            "achievable": "하루에 최소 3시간 이상 투자하기",
                            "relevant": "프로젝트 경험을 통한 기술 및 협업 역량 향상 기대",
                            "timeBound": "2025년 2월 10일 - 12/23~1/10: 00부분까지 완료, 1/12~1/26: API 연결",
                            "otherPlans": "매주 토요일 강남역 대면 미팅 참석"
                        }
                    ]
                    ```
                    """)

    public ApiResponse<String> editPlanner(@LoginMember Member member, @RequestBody CreatePlannerListDTO createPlannerListDTO) {
        plannerCommandService.editPlanner(member, createPlannerListDTO);
        return ApiResponse.onSuccess("플래너 수정 완료");
    }

    @DeleteMapping
    @Operation(
            summary = "플래너 삭제",
            description = """
                    플래너를 삭제합니다.
                    """)
    public ApiResponse<String> deletePlanner(@LoginMember Member member){
        plannerCommandService.deletePlanner(member);
        return ApiResponse.onSuccess("플래너 삭제 완료");
    }

    @GetMapping
    @Operation(
            summary = "플래너 조회",
            description = """
                    플래너를 조회합니다.
                    없는 값은 null을 리턴합니다.
                    ### Example JSON:
                    ```json
                    planners : [
                        {
                            "activityName": "React.js로 커리어 메이트 00파트 개발",
                            "startTime": "2025-12-23T09:00:00",
                            "endTime": "2025-12-23T18:00:00",
                            "specifics": "React.js를 활용한 프로젝트 5개 기능 구현",
                            "measurable": "개인 프로젝트를 GitHub에 배포 + 최소 5개 기능 구현",
                            "achievable": "하루에 최소 3시간 이상 투자하기",
                            "relevant": "프로젝트 경험을 통한 기술 및 협업 역량 향상 기대",
                            "timeBound": "2025년 2월 10일 - 12/23~1/10: 00부분까지 완료, 1/12~1/26: API 연결",
                            "otherPlans": "매주 토요일 강남역 대면 미팅 참석"
                        },{
                            "activityName": "React.js로 커리어 메이트 00파트 개발",
                            "startTime": "2025-12-23T09:00:00",
                            "endTime": "2025-12-23T18:00:00",
                            "specifics": "React.js를 활용한 프로젝트 5개 기능 구현",
                            "measurable": "개인 프로젝트를 GitHub에 배포 + 최소 5개 기능 구현",
                            "achievable": "하루에 최소 3시간 이상 투자하기",
                            "relevant": "프로젝트 경험을 통한 기술 및 협업 역량 향상 기대",
                            "timeBound": "2025년 2월 10일 - 12/23~1/10: 00부분까지 완료, 1/12~1/26: API 연결",
                            "otherPlans": "매주 토요일 강남역 대면 미팅 참석"
                    }]
                    ```
                    """)
    public ApiResponse<PlannerListResponseDTO> getPlanner(@LoginMember Member member){
        return ApiResponse.onSuccess(plannerQueryService.getPlannerByMember(member));
    }


}
