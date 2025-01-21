package UMC.career_mate.domain.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberEducationLevel {
    MIDDLE, // 중학교 이하
    HIGH, // 고등학교
    JUNIOR_COLLEGE, // 전문대학
    UNIVERSITY, // 대학교
    MASTER, // 석사
    DOCTOR //박사
    ;
}
