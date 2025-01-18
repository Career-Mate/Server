package UMC.career_mate.global.response.result.code;

import UMC.career_mate.global.response.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonResultCode implements ResultCode {
    EXAMPLE_RESULT_CODE(200,"SE000", "엔티티 별로 이런식으로 작성해주세요"),

    ;

    private final int status;
    private final String code;
    private final String message;
}
