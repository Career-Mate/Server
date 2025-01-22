package UMC.career_mate.global.response.result.code;

import UMC.career_mate.global.response.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonResultCode implements ResultCode {
    EXAMPLE_RESULT_CODE(200,"SE000", "엔티티 별로 이런식으로 작성해주세요"),

    //member
    MEMBER_INFO(200, "SME000", "회원 정보 조회에 성공하였습니다."),

    //job
    GET_JOB_LIST(200, "SJO000", "모든 직무 조회에 성공하였습니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
