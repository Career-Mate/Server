package UMC.career_mate.global.response.result.code;

import UMC.career_mate.global.response.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonResultCode implements ResultCode {
    EXAMPLE_RESULT_CODE(200,"SE000", "엔티티 별로 이런식으로 작성해주세요"),

    //member
    MEMBER_INFO(200, "SM000", "회원 정보 조회에 성공하였습니다."),
    MODIFY_MEMBER_INFO(200, "SM001", "프로필 수정에 성공하였습니다."),

    //job
    GET_JOB_LIST(200, "SJO000", "모든 직무 조회에 성공하였습니다."),

    // Template 도메인
    GET_TEMPLATE(200,"ST000", "성공적으로 직무에 맞는 템플릿을 조회했습니다."),

    // Answer 도메인
    CREATE_ANSWER_LIST(200,"SA000", "성공적으로 답변을 저장했습니다."),
    GET_ANSWER_LIST(200,"SA000", "성공적으로 답변을 조회했습니다."),
    UPDATE_ANSWER_LIST(200,"SA000", "성공적으로 답변을 수정했습니다."),
    GET_ANSWER_COMPLETION_STATUS(200,"SA000", "성공적으로 답변 작성 여부를 조회했습니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
