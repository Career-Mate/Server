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

    //member
    NOT_FOUND_BY_MEMBER_ID(400, "EME000", "해당 memberId를 가진 회원이 존재하지 않습니다."),
    NOT_FOUND_BY_CLIENT_ID(400, "EME001", "해당 clientId를 가진 회원이 존재하지 않습니다."),
    NOT_FOUND_BY_ID_AND_CLIENT_ID(400, "EME002", "해당 memberId와 clientId를 가진 회원이 존재하지 않습니다."),

    //job
    NOT_FOUND_BY_JOB_ID(400, "EJB000", "해당 ID의 직무가 존재하지 않습니다."),

    //Planner 도메인
    PLANNER_NOT_EXISTS(400, "EPL001","유저의 플래너가 존재하지 않습니다. 먼저 POST로 생성해주세요."),
    PLANNER_EXISTS(400, "EPL002","유저의 플래너가 존재합니다. PATCH로 수정해주세요."),

    // Recruit 도메인
    NOT_FOUND_RECRUIT(400, "ERE000", "해당 채용 공고를 찾을 수 없습니다."),

    // Job 도메인
    NOT_FOUND_JOB(400, "ERJ000", "해당 직업을 찾을 수 없습니다."),

    // Template 도메인
    NOT_FOUND_TEMPLATE(400, "ERT000", "해당 템플릿을 찾을 수 없습니다."),

    // Question 도메인
    NOT_FOUND_QUESTION(400, "ERQ000", "해당 질문을 찾을 수 없습니다."),

    // Login 관련
    INVALID_SOCIAL_PLATFORM(403, "ELO000", "지원하지 않는 로그인 플랫폼임니다."),

    // token 관련
    INVALID_TOKEN(401, "ETK000", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(401, "ETK001", "만료된 토큰입니다."),
    FAILED_SIGNATURE_TOKEN(401, "ETK002", "토큰 서명 검증에 실패했습니다."),
    NOT_FOUND_REFRESH_TOKEN_BY_MEMBER_ID(400, "ETK003", "해당 회원 ID의 refresh token이 존재하지 않습니다."),
    ;
    private final int status;
    private final String code;
    private final String message;
}
