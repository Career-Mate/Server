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
    PLANNER_EXISTS(400, "EPL002","유저의 플래너가 존재합니다. 프로필 작성 완료시 플래너가 자동 생성되기 때문에,PATCH API를 사용해주시면 되겠습니다"),

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
    INVALID_TOKEN(4001, "ETK000", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(4002, "ETK001", "만료된 토큰입니다."),
    NO_ACCESS_TOKEN(4003, "ETK004", "access 토큰이 존재하지 않습니다."),
    FAILED_SIGNATURE_TOKEN(401, "ETK002", "토큰 서명 검증에 실패했습니다."),
    NOT_FOUND_REFRESH_TOKEN_BY_MEMBER_ID(400, "ETK003", "해당 회원 ID의 refresh token이 존재하지 않습니다."),

    // Answer 도메인
    NOT_FOUND_ANSWER(400, "EAN000", "해당 답변을 찾을 수 없습니다."),
    TOO_MANY_ANSWERS(400, "EAN001", "커리어는 두개 이상 작성할 수 없습니다."),

    // ContentScrap 도메인
    NOT_FOUND_CONTENT(400, "ESC001", "해당 콘텐츠를 찾을 수 없습니다."),
    DUPLICATE_SCRAP(400, "ESC002", "이미 스크랩된 콘텐츠입니다."),
    NOT_FOUND_SCRAP(400, "ESC003", "스크랩이 존재하지 않습니다."),

    // RecruitScrap 도메인
    DUPLICATE_RECRUIT_SCRAP(400, "ERSC001", "이미 스크랩된 채용 공고입니다."),
    NOT_FOUND_RECRUIT_SCRAP(400, "ERSC002", "해당 채용 공고 스크랩을 찾을 수 없습니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
}
