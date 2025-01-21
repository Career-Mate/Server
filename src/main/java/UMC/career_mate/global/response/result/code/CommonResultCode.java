package UMC.career_mate.global.response.result.code;

import UMC.career_mate.global.response.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonResultCode implements ResultCode {
    // Template 도메인
    GET_TEMPLATE(200,"ST000", "성공적으로 직무에 맞는 템플릿을 조회했습니다."),

    // Answer 도메인
    CREATE_ANSWER(200,"ST000", "성공적으로 답변을 저장했습니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
