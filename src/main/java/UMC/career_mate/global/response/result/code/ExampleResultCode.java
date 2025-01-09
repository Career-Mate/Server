package UMC.career_mate.global.response.result.code;

import UMC.career_mate.global.response.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExampleResultCode implements ResultCode {
    /*
    code는 S(uccess) + 엔티티 제일 첫 알파뱃 + 순서 로 작성해주시면 됩니다.
    ex) Member 도메인의 경우
        SM000
        SM001
     */
    EXAMPLE_RESULT_CODE(200,"SE000", "엔티티 별로 이런식으로 작성해주세요"),

    ;

    private final int status;
    private final String code;
    private final String message;
}
