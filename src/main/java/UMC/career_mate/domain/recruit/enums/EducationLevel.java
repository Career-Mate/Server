package UMC.career_mate.domain.recruit.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EducationLevel {
    NO_REQUIREMENT(0, "학력무관"),
    HIGH_SCHOOL_GRADUATE(1, "고등학교졸업"),
    COLLEGE_GRADUATE_2_3_YEAR(2, "대학졸업(2,3년)"),
    UNIVERSITY_GRADUATE_4_YEAR(3, "대학교졸업(4년)"),
    MASTER_GRADUATE(4, "석사졸업"),
    DOCTOR_GRADUATE(5, "박사졸업"),
    HIGH_SCHOOL_OR_HIGHER(1, "고등학교졸업이상"),
    COLLEGE_2_3_YEAR_OR_HIGHER(2, "대학졸업(2,3년)이상"),
    UNIVERSITY_4_YEAR_OR_HIGHER(3, "대학교졸업(4년)이상"),
    MASTER_OR_HIGHER(4, "석사졸업이상");

    private final int code;
    private final String description;

    public static EducationLevel fromDescription(String description) {
        for (EducationLevel educationLevel : EducationLevel.values()) {
            if (educationLevel.getDescription().equals(description)) {
                return educationLevel;
            }
        }
        return null;
    }
}

