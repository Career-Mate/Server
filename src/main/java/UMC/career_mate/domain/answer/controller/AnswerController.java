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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static UMC.career_mate.global.response.result.code.CommonResultCode.*;

@RestController
@RequestMapping("/answers")
@Tag(name = "답변 API", description = "답변 도메인의 API 입니다.")
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerCommandService answerCommandService;
    private final AnswerQueryService answerQueryService;

    @PostMapping
    @Operation(
            summary = "커리어 작성 API",
            description =
                    """
                            커리어를 작성하는 API입니다.
                            커리어를 처음 작성할 때, 반드시 2개의 `answerInfoList` 데이터를 요청해야 합니다.
                            입력하지 않을 경우엔 빈 문자열로 요청할 수 있습니다.
                            ## 하나의 커리어만 작성하는 경우
                            ### Example REQUEST JSON:
                            ```json
                            {
                              "answerGroupDTOList": [
                                {
                                  "sequence": 1,
                                  "answerInfoDTOList": [
                                    {
                                      "questionId": 31,
                                      "content": "카카오뱅크 / 대출상품기획팀"
                                    },
                                    {
                                      "questionId": 32,
                                      "content": "2022.03.01~2022.08.01"
                                    },
                                    {
                                      "questionId": 33,
                                      "content": "회사명 / IoT 디바이스 팀"
                                    },
                                    {
                                      "questionId": 34,
                                      "content": "IoT 디바이스 통신 프로토콜을 설계하며, 효율적인 데이터 전송 방식에 대해 배웠습니다."
                                    },
                                    {
                                      "questionId": 35,
                                      "content": "스마트 홈 서비스의 핵심 모듈을 개발하며, 사용자 경험 중심의 설계 중요성을 깨달았습니다."
                                    },
                                    {
                                      "questionId": 36,
                                      "content": "팀원들과의 코드 리뷰를 통해 문제를 다양한 시각으로 바라보는 법을 배웠습니다."
                                    },
                                    {
                                      "questionId": 37,
                                      "content": "복잡한 문제를 해결하는 과정에서 논리적인 사고력과 협업 능력이 향상되었습니다."
                                    }
                                  ]
                                },
                                {
                                  "sequence": 2,
                                  "answerInfoDTOList": [
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
                                    },
                                    {
                                      "questionId": 36,
                                      "content": ""
                                    },
                                    {
                                      "questionId": 37,
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
                                                        @RequestPart(value = "data") @Valid AnswerCreateOrUpdateDTO answerCreateOrUpdateDTO,
                                                        @RequestPart(value = "image_1", required = false) MultipartFile image1,
                                                        @RequestPart(value = "image_2", required = false) MultipartFile image2) throws IOException {
        answerCommandService.saveAnswerList(member, answerCreateOrUpdateDTO, image1, image2);
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
    public ApiResponse<List<AnswerInfoListDTO>> getAnswerList(@LoginMember Member member,
                                                              @RequestParam("templateType") TemplateType templateType) {
        return ApiResponse.onSuccess(GET_ANSWER_LIST, answerQueryService.getAnswersByTemplateType(member, templateType));
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
                              "answerGroupDTOList": [
                                {
                                  "sequence": 1,
                                  "answerInfoDTOList": [
                                    {
                                      "questionId": 31,
                                      "content": "네이버 / 데이터 분석팀"
                                    },
                                    {
                                      "questionId": 32,
                                      "content": "2021.05.01~2022.02.28"
                                    },
                                    {
                                      "questionId": 33,
                                      "content": "스타트업 / 마케팅 데이터 분석팀"
                                    },
                                    {
                                      "questionId": 34,
                                      "content": "A/B 테스트를 설계하여 마케팅 캠페인의 효과를 분석하고 최적의 전략을 도출했습니다."
                                    },
                                    {
                                      "questionId": 35,
                                      "content": "데이터 시각화를 통해 인사이트를 제공하며, 의사결정 과정을 지원하는 방법을 배웠습니다."
                                    },
                                    {
                                      "questionId": 36,
                                      "content": "Python과 SQL을 활용한 데이터 분석 프로젝트를 진행하며, 데이터 처리 및 분석 역량을 키웠습니다."
                                    },
                                    {
                                      "questionId": 37,
                                      "content": "데이터 기반 의사결정의 중요성을 체감하며, 문제 해결력을 향상시킬 수 있었습니다."
                                    }
                                  ]
                                },
                                {
                                  "sequence": 2,
                                  "answerInfoDTOList": [
                                    {
                                      "questionId": 31,
                                      "content": "쿠팡 / 물류 최적화 팀"
                                    },
                                    {
                                      "questionId": 32,
                                      "content": "2022.06.01~2022.12.31"
                                    },
                                    {
                                      "questionId": 33,
                                      "content": "이커머스 기업 / 물류 데이터 분석팀"
                                    },
                                    {
                                      "questionId": 34,
                                      "content": "배송 최적화를 위한 데이터 분석을 진행하며, 머신러닝을 활용한 수요 예측 모델을 개발했습니다."
                                    },
                                    {
                                      "questionId": 35,
                                      "content": "물류 데이터 분석을 통해 비용 절감과 효율성을 높이는 경험을 했습니다."
                                    },
                                    {
                                      "questionId": 36,
                                      "content": "팀원들과 협업하여 데이터 기반 개선안을 도출하고, 이를 현업에 적용하는 과정을 배웠습니다."
                                    },
                                    {
                                      "questionId": 37,
                                      "content": "데이터를 기반으로 문제를 해결하는 과정에서 논리적 사고력과 커뮤니케이션 능력을 키웠습니다."
                                    }
                                  ]
                                }
                              ]
                            }
                            ```
                            """
    )
    public ApiResponse<CommonResultCode> updateAnswerList(@LoginMember Member member,
                                                          @RequestPart("data") @Valid AnswerCreateOrUpdateDTO answerCreateOrUpdateDTO,
                                                          @RequestPart(value = "image_1", required = false) MultipartFile image1,
                                                          @RequestPart(value = "image_2", required = false) MultipartFile image2) throws IOException {
        answerCommandService.updateAnswerList(member, answerCreateOrUpdateDTO, image1, image2);
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
