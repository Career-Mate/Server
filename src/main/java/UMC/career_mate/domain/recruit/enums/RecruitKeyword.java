package UMC.career_mate.domain.recruit.enums;

import java.util.List;

public enum RecruitKeyword {

    BACKEND_SPRING {
        @Override
        public List<String> getIncludeKeywordList() {
            return List.of("Back", "Backend", "Back-end", "BACK", "백엔드", "서버", "시스템");
        }

        @Override
        public List<String> getExcludeKeywordList() {
            return List.of("Node", "Node.js", "NODE", "javascript", "Python", "Django", "C++", "PHP");
        }
    },
    BACKEND_NODE {
        @Override
        public List<String> getIncludeKeywordList() {
            return List.of("Back", "Backend", "Back-end", "BACK", "백엔드", "서버", "시스템");
        }

        @Override
        public List<String> getExcludeKeywordList() {
            return List.of("java", "JAVA", "spring", "Spring", "SPRING", "Python", "Django", "C++", "PHP");
        }
    },
    BACKEND_DJANGO {
        @Override
        public List<String> getIncludeKeywordList() {
            return List.of("Back", "Backend", "Back-end", "BACK", "백엔드", "서버", "시스템");
        }

        @Override
        public List<String> getExcludeKeywordList() {
            return List.of("java", "JAVA", "spring", "Spring", "SPRING", "javascript", "Node",
                "Node.js", "NODE", "C++", "PHP");
        }
    },
    FRONTEND {
        @Override
        public List<String> getIncludeKeywordList() {
            return List.of("Front", "FRONT", "Frontend", "Front-end", "프론트엔드", "프론트");
        }

        @Override
        public List<String> getExcludeKeywordList() {
            return null;
        }
    },
    DESIGNER {
        @Override
        public List<String> getIncludeKeywordList() {
            return List.of("웹 디자이너", "웹디자이너", "UI", "UX", "디자인", "design", "DESIGN");
        }

        @Override
        public List<String> getExcludeKeywordList() {
            return List.of("영상디자인", "영상 디자인"); // 굿즈 디자인은? 뺴야하는가
        }
    },
    PM {
        @Override
        public List<String> getIncludeKeywordList() {
            return List.of("PM", "Project Manager", "Product Manager", "PROJECT MANAGER",
                "PRODUCT MANAGER", "프로젝트 매니저", "프로덕트 매니저");
        }

        @Override
        public List<String> getExcludeKeywordList() {
            return null;
        }
    }

    ;

    public abstract List<String> getIncludeKeywordList();
    public abstract List<String> getExcludeKeywordList();
}
