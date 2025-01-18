package UMC.career_mate.global.response.exception.code;

import UMC.career_mate.global.response.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
    BAD_REQUEST(400, "ECM000", "잘못된 요청입니다."),
    UNAUTHORIZED(401, "ECM001", "인증이 필요합니다."),
    FORBIDDEN(403, "ECM002", "금지된 요청입니다."),

    //job
    NOT_FOUND_BY_JOB_ID(400, "EJB000", "해당 ID의 직무가 존재하지 않습니다."),
    ;
    private final int status;
    private final String code;
    private final String message;
}
