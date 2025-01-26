package UMC.career_mate.domain.answer.controller;

import UMC.career_mate.domain.answer.dto.request.AnswerCreateOrUpdateDTO;
import UMC.career_mate.domain.answer.dto.response.AnswerCompletionStatusInfoListDTO;
import UMC.career_mate.domain.answer.dto.response.AnswerInfoListDTO;
import UMC.career_mate.domain.answer.service.AnswerCommandService;
import UMC.career_mate.domain.answer.service.AnswerQueryService;
import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.template.enums.TemplateType;
import UMC.career_mate.global.annotation.LoginMember;
import UMC.career_mate.global.response.ApiResponse;
import UMC.career_mate.global.response.result.code.CommonResultCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static UMC.career_mate.global.response.result.code.CommonResultCode.*;

@RestController
@RequestMapping("/answers")
@Tag(name = "05. 답변 API", description = "답변 도메인의 API 입니다.")
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerCommandService answerCommandService;
    private final AnswerQueryService answerQueryService;

    @PostMapping
    @Operation(
            summary = "커리어 작성 API",
            description =
                    """
                            커리어를 작성하는 API 입니다.
                            커리어를 처음 작성할 때, 반드시 2개의  `answerInfoList` 데이터를 요청해야 합니다. 입력하지 않을 경우엔 빈 문자열로 요청을 할 수 있습니다.
                            ## 하나의 커리어만 작성하는 경우
                            ### Example REQUEST JSON:
                            ```json
                            {
                              "answerList": [
                                {
                                  "sequence": 1,
                                  "answerInfoList": [
                                    {
                                      "questionId": 30,
                                      "content": "삼성전자 / IoT 개발팀"
                                    },
                                    {
                                      "questionId": 31,
                                      "content": "백엔드 개발자"
                                    },
                                    {
                                      "questionId": 32,
                                      "content": "2022.03.01~2022.08.01"
                                    },
                                    {
                                      "questionId": 33,
                                      "content": "IoT 디바이스 통신 프로토콜을 설계하며, 효율적인 데이터 전송 방식에 대해 배웠습니다."
                                    },
                                    {
                                      "questionId": 34,
                                      "content": "스마트 홈 서비스의 핵심 모듈을 개발하며, 사용자 경험 중심의 설계 중요성을 깨달았습니다."
                                    },
                                    {
                                      "questionId": 35,
                                      "content": "팀원들과의 코드 리뷰를 통해 문제를 다양한 시각으로 바라보는 법을 배웠습니다."
                                    }
                                  ]
                                },
                                {
                                  "sequence": 2,
                                  "answerInfoList": [
                                    {
                                      "questionId": 30,
                                      "content": ""
                                    },
                                    {
                                      "questionId": 31,
                                      "content": ""
                                    },
                                    {
                                      "questionId": 32,
                                      "content": ""
                                    },
                                    {
                                      "questionId": 33,
                                      "content": ""
                                    },
                                    {
                                      "questionId": 34,
                                      "content": ""
                                    },
                                    {
                                      "questionId": 35,
                                      "content": ""
                                    }
                                  ]
                                }
                              ]
                            }
                            ```
                            """
    )
    public ApiResponse<CommonResultCode> saveAnswerList(@LoginMember Member member,
                                                        @Valid @RequestBody AnswerCreateOrUpdateDTO answerCreateOrUpdateDTO) {
        answerCommandService.saveAnswerList(member, answerCreateOrUpdateDTO);
        return ApiResponse.onSuccess(CREATE_ANSWER_LIST);
    }

    @GetMapping
    @Operation(
            summary = "템플릿 타입에 맞는 커리어 조회 API",
            description =
                    """
                            템플릿 타입에 맞는 나의 커리어를 조회하는 API 입니다. \
                            템플릿 타입은 다음과 같습니다.\s
                            1. 인턴 경험 (INTERN_EXPERIENCE)\s
                            2. 프로젝트 경험 (PROJECT_EXPERIENCE)\s
                            3. 기타 활동 (OTHER_ACTIVITIES)\s
                            4. 보유 기술 (TECHNICAL_SKILLS)\s
                            5. 최종 정리 (SUMMARY)
                            """)
    public ApiResponse<List<AnswerInfoListDTO>> getAnswerList(@RequestParam Long memberId,
                                                              @RequestParam("templateType") TemplateType templateType) {
        return ApiResponse.onSuccess(GET_ANSWER_LIST, answerQueryService.getAnswersByTemplateType(memberId, templateType));
    }

    @PatchMapping
    @Operation(
            summary = "커리어 수정 API",
            description =
                    """
                            커리어를 수정하는 API 입니다.
                            커리어를 수정할 때, 반드시 2개의  `answerInfoList` 데이터를 요청해야 합니다. 입력하지 않을 경우엔 빈 문자열로 요청을 할 수 있습니다.
                            ### Example REQUEST JSON:
                            ```json
                            {
                              "answerList": [
                                {
                                  "sequence": 1,
                                  "answerInfoList": [
                                    {
                                      "questionId": 30,
                                      "content": "삼성전자 / IoT 개발팀"
                                    },
                                    {
                                      "questionId": 31,
                                      "content": "백엔드 개발자"
                                    },
                                    {
                                      "questionId": 32,
                                      "content": "2022.03.01~2022.08.01"
                                    },
                                    {
                                      "questionId": 33,
                                      "content": "IoT 디바이스 통신 프로토콜을 설계하며, 효율적인 데이터 전송 방식에 대해 배웠습니다."
                                    },
                                    {
                                      "questionId": 34,
                                      "content": "스마트 홈 서비스의 핵심 모듈을 개발하며, 사용자 경험 중심의 설계 중요성을 깨달았습니다."
                                    },
                                    {
                                      "questionId": 35,
                                      "content": "팀원들과의 코드 리뷰를 통해 문제를 다양한 시각으로 바라보는 법을 배웠습니다."
                                    }
                                  ]
                                },
                                {
                                  "sequence": 2,
                                  "answerInfoList": [
                                    {
                                      "questionId": 30,
                                      "content": "LG CNS / 클라우드 플랫폼 개발팀"
                                    },
                                    {
                                      "questionId": 31,
                                      "content": "풀스택 개발자"
                                    },
                                    {
                                      "questionId": 32,
                                      "content": "2021.09.01~2022.02.01"
                                    },
                                    {
                                      "questionId": 33,
                                      "content": "클라우드 기반 서비스 배포 자동화를 구현하며 DevOps의 핵심 개념을 익혔습니다."
                                    },
                                    {
                                      "questionId": 34,
                                      "content": "대규모 사용자 트래픽을 처리하며 안정적인 시스템 운영 경험을 쌓았습니다."
                                    },
                                    {
                                      "questionId": 35,
                                      "content": "다양한 클라우드 서비스를 연동하며 기술적 시야를 넓힐 수 있었습니다."
                                    }
                                  ]
                                }
                              ]
                            }
                            ```
                            """
    )
    public ApiResponse<CommonResultCode> updateAnswerList(@LoginMember Member member,
                                                          @Valid @RequestBody AnswerCreateOrUpdateDTO answerCreateOrUpdateDTO) {
        answerCommandService.updateAnswerList(member, answerCreateOrUpdateDTO);
        return ApiResponse.onSuccess(UPDATE_ANSWER_LIST);
    }

    @GetMapping("/completion-status")
    @Operation(
            summary = "커리어 작성 진행 상태 API",
            description =
                    """
                            특정 템플릿 타입에 대해 커리어 작성이 완료되었는지 확인하는 API입니다.
                            템플릿 타입은 다음과 같습니다.\s
                            1. 인턴 경험 (INTERN_EXPERIENCE)\s
                            2. 프로젝트 경험 (PROJECT_EXPERIENCE)\s
                            3. 기타 활동 (OTHER_ACTIVITIES)\s
                            4. 보유 기술 (TECHNICAL_SKILLS)\s
                            5. 최종 정리 (SUMMARY)
                            
                            ### Example Response JSON:
                            ```json
                            {
                              "status": 200,
                              "code": "SA000",
                              "message": "성공적으로 답변 작성 여부를 조회했습니다.",
                              "data": {
                                "isAllCompleted": false,
                                "answerCompletionStatusInfoDTOList": [
                                  {
                                    "templateType": "INTERN_EXPERIENCE",
                                    "isComplete": true
                                  },
                                  {
                                    "templateType": "PROJECT_EXPERIENCE",
                                    "isComplete": false
                                  },
                                  {
                                    "templateType": "OTHER_ACTIVITIES",
                                    "isComplete": false
                                  },
                                  {
                                    "templateType": "TECHNICAL_SKILLS",
                                    "isComplete": false
                                  },
                                  {
                                    "templateType": "SUMMARY",
                                    "isComplete": false
                                  }
                                ]
                              }
                            }
                            ```
                            """
    )
    public ApiResponse<AnswerCompletionStatusInfoListDTO> getAnswerCompletionStatus(@LoginMember Member member) {
        return ApiResponse.onSuccess(GET_ANSWER_COMPLETION_STATUS, answerQueryService.getAnswerCompletionStatus(member));
    }
}
