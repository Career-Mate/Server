package UMC.career_mate.global.response.exception.code;

import UMC.career_mate.global.response.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExampleErrorCode implements ErrorCode {
    /*
    code는 E(rror) + 엔티티 제일 첫 알파뱃 + 순서로 작성해주시면 됩니다.
    ex) Member 도메인의 경우
        EM000
        EM001
     */
    EXAMPLE_ERROR_CODE(400, "EE000", "에러 발생시 사용할 코드 예시입니다."),

    ;
    private final int status;
    private final String code;
    private final String message;
}
