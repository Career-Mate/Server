package UMC.career_mate.domain.member.entity;

import lombok.Getter;

@Getter
public enum EducationLevel {
    MIDDLE, //중학교 이하
    HIGH, // 고등학교
    JUNIOR_COLLEGE, // 전문대학
    UNIVERSITY, // 대학
    MASTER, // 석사
    DOCTOR // 박사
    ;
}
