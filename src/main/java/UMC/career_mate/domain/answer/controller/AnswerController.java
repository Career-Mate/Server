package UMC.career_mate.domain.answer.controller;

import UMC.career_mate.domain.answer.dto.request.AnswerRequest;
import UMC.career_mate.domain.answer.service.AnswerCommandService;
import UMC.career_mate.global.response.ApiResponse;
import UMC.career_mate.global.response.result.code.CommonResultCode;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static UMC.career_mate.global.response.result.code.CommonResultCode.CREATE_ANSWER;

@RestController
@RequestMapping("/answers")
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerCommandService answerCommandService;

    @PostMapping
    @Operation(
            summary = "커리어 작성 API",
            description =
                    """
                    커리어를 작성하는 API 입니다.
                    ### Example JSON:
                    ```json
                    {
                      "answerList": [
                        {
                          "answerInfoList": [
                            {
                              "questionId": 1,
                              "content": "카카오뱅크 / 프론트엔드 개발팀"
                            },
                            {
                              "questionId": 2,
                              "content": "프론트엔드 개발자"
                            },
                            {
                              "questionId": 3,
                              "content": "2024.01.01~2024.01.15"
                            },
                            {
                              "questionId": 4,
                              "content": "새로운 프론트엔드 기술을 익히고, 팀과의 협업에서 커뮤니케이션의 중요성을 배웠습니다."
                            },
                            {
                              "questionId": 5,
                              "content": "카카오뱅크의 주요 서비스를 담당하며, 실제 사용자들에게 영향을 미치는 프로젝트에 기여한 경험이 의미 있었습니다."
                            },
                            {
                              "questionId": 6,
                              "content": "코드 리뷰 과정을 통해 클린 코드 작성 능력이 향상되었고, 효율적인 문제 해결 방법을 배웠습니다."
                            }
                          ]
                        }, ...
                      ]
                    }
                    ```
                    """
    )
    public ApiResponse<CommonResultCode> saveAnswerList(@Valid @RequestBody AnswerRequest request) {
        answerCommandService.saveAnswerList(request);
        return ApiResponse.onSuccess(CREATE_ANSWER);
    }
}
