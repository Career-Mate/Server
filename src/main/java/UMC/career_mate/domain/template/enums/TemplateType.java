package UMC.career_mate.domain.template.enums;

import lombok.Getter;

@Getter
public enum TemplateType {
    INTERN_EXPERIENCE("인턴 경험"),
    PROJECT_EXPERIENCE("프로젝트 경험"),
    OTHER_ACTIVITIES("기타 활동"),
    TECHNICAL_SKILLS("보유 기술"),
    SUMMARY("최종 정리");
    ;

    private final String description;

    TemplateType(String description) {
        this.description = description;
    }
}
