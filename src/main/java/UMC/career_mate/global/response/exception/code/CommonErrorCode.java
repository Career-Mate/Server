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

    //Planner 도메인
    PLANNER_NOT_EXISTS(400, "EPL001","유저의 플래너가 존재하지 않습니다. 먼저 POST로 생성해주세요."),
    PLANNER_EXISTS(400, "EPL4002","유저의 플래너가 존재합니다. PATCH로 수정해주세요."),
    ;
    private final int status;
    private final String code;
    private final String message;
}
