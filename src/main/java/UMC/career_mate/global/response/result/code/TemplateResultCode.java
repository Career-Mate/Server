package UMC.career_mate.global.response.result.code;

import UMC.career_mate.global.response.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TemplateResultCode implements ResultCode {
    GET_TEMPLATE(200,"ST000", "성공적으로 직무에 맞는 템플릿을 조회했습니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
