package UMC.career_mate.domain.recruit.enums;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JobCode {

    BACK_END("백엔드/서버개발", 84, true), // 백엔드 데이터 가져오는 코드
    FRONT_END("프론트엔드", 92, true), // 프론트엔드 데이터 가져오는 코드
    APP_DESIGN("앱디자인", 1499, true), // 디자이너 데이터 가져오는 코드
    WEB_DESIGN("웹디자인", 1502, true), // 디자이너 데이터 가져오는 코드
    UI_UX_DESIGN("UI/UX디자인", 1529, true), // 디자이너 데이터 가져오는 코드
    PM("개발PM", 2247, true), // PM 데이터 가져오는 코드
    APP_DEVELOPER("앱개발", 86, false),
    WEB_DEVELOPER("웹개발", 87, false),
    SPRING("Spring", 291, false),
    SPRING_BOOT("SpringBoot", 292, false),
    REACT("React", 277, false),
    REACT_NATIVE("React-Native", 278, false),
    SWIFT("Swift", 298, false),
    TYPESCRIPT("TypeScript", 302, false),
    NODE("Node.js", 258, false),
    FULL_STACK("풀스택", 2232, false),
    ANDROID("Android", 195, false),
    DJANGO("Django", 213, false),
    FLUTTER("Flutter", 220, false),
    IOS("iOS", 234, false),
    JAVA("Java", 235, false),
    JAVASCRIPT("Javascript", 236, false),
    JPA("JPA", 238, false),
    KOTLIN("Kotlin", 243, false),
    PYTHON("Python", 272, false),
    PUBLISHER("퍼블리셔", 91, false),
    FIGMA("Figma", 1609, false),
    GUI("GUI", 1589, false)
    ;

    private final String name;
    private final int code;
    private final boolean isSearchTarget;

    public static List<JobCode> getTrueSearchTarget() {
        List<JobCode> jobCodeList = new ArrayList<>();
        for (JobCode jobCode : JobCode.values()) {
            if (jobCode.isSearchTarget()) {
                jobCodeList.add(jobCode);
            }
        }
        return jobCodeList;
    }
}
