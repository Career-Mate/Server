package UMC.career_mate.domain.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EducationStatus {
    ENROLLED, // 재학
    ON_LEAVE, // 휴학
    GRADUATED, // 졸업
    COMPLETED,  // 수료
    ;
}
