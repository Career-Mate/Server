package UMC.career_mate.domain.recruit.enums;

import UMC.career_mate.domain.job.Job;
import java.util.List;

public enum RecruitKeyword {

    BACKEND {
        @Override
        public List<String> getIncludeTitleKeywordList() {
            return List.of("Back", "Backend", "Back-end", "백엔드", "서버", "시스템");
        }

        @Override
        public List<String> getExcludeTitleKeywordList() {
            return null;
        }

        @Override
        public List<String> getIncludeHashtagKeywordList() {
            return null;
        }
    }
    ,
    BACKEND_SPRING {
        @Override
        public List<String> getIncludeTitleKeywordList() {
            return List.of("Back", "Backend", "Back-end", "백엔드", "서버", "시스템");
        }

        @Override
        public List<String> getExcludeTitleKeywordList() {
            return List.of("Node", "Node.js", "javascript", "Python", "Django", "C++", "PHP", "C#");
        }

        @Override
        public List<String> getIncludeHashtagKeywordList() {
            return List.of("Spring", "SpringBoot");
        }
    },
    BACKEND_NODE {
        @Override
        public List<String> getIncludeTitleKeywordList() {
            return List.of("Back", "Backend", "Back-end", "백엔드", "서버", "시스템");
        }

        @Override
        public List<String> getExcludeTitleKeywordList() {
            return List.of("JAVA", "Spring", "Python", "Django", "C++", "PHP", "C#");
        }

        @Override
        public List<String> getIncludeHashtagKeywordList() {
            return List.of("Node.js");
        }
    },
    BACKEND_DJANGO {
        @Override
        public List<String> getIncludeTitleKeywordList() {
            return List.of("Back", "Backend", "Back-end", "백엔드", "서버", "시스템");
        }

        @Override
        public List<String> getExcludeTitleKeywordList() {
            return List.of("JAVA", "Spring", "javascript", "Node", "Node.js", "C++", "PHP", "C#");
        }

        @Override
        public List<String> getIncludeHashtagKeywordList() {
            return List.of("Django", "Python");
        }
    },
    FRONTEND {
        @Override
        public List<String> getIncludeTitleKeywordList() {
            return List.of("Front", "Frontend", "Front-end", "프론트엔드", "프론트");
        }

        @Override
        public List<String> getExcludeTitleKeywordList() {
            return null;
        }

        @Override
        public List<String> getIncludeHashtagKeywordList() {
            return null;
        }
    },
    FRONTEND_REACT {
        @Override
        public List<String> getIncludeTitleKeywordList() {
            return List.of("React");
        }

        @Override
        public List<String> getExcludeTitleKeywordList() {
            return null;
        }

        @Override
        public List<String> getIncludeHashtagKeywordList() {
            return List.of("React");
        }
    },
    FRONTEND_IOS {
        @Override
        public List<String> getIncludeTitleKeywordList() {
            return List.of("iOS");
        }

        @Override
        public List<String> getExcludeTitleKeywordList() {
            return null;
        }

        @Override
        public List<String> getIncludeHashtagKeywordList() {
            return List.of("iOS", "Swift");
        }
    },
    FRONTEND_ANDROID {
        @Override
        public List<String> getIncludeTitleKeywordList() {
            return List.of("Android");
        }

        @Override
        public List<String> getExcludeTitleKeywordList() {
            return null;
        }

        @Override
        public List<String> getIncludeHashtagKeywordList() {
            return List.of("Android");
        }
    },
    DESIGNER {
        @Override
        public List<String> getIncludeTitleKeywordList() {
            return List.of("웹 디자이너", "웹디자이너", "UI", "UX", "디자인", "design");
        }

        @Override
        public List<String> getExcludeTitleKeywordList() {
            return List.of("영상디자인", "영상 디자인");
        }

        @Override
        public List<String> getIncludeHashtagKeywordList() {
            return List.of("모바일디자인", "앱디자인", "웹디자인", "UI/UX디자인");
        }
    },
    PM {
        @Override
        public List<String> getIncludeTitleKeywordList() {
            return List.of("PM", "Project Manager", "Product Manager", "프로젝트 매니저", "프로덕트 매니저");
        }

        @Override
        public List<String> getExcludeTitleKeywordList() {
            return null;
        }

        @Override
        public List<String> getIncludeHashtagKeywordList() {
            return List.of("개발PM", "PM(프로젝트매니저)", "PL(프로젝트리더)");
        }
    }

    ;

    public static RecruitKeyword getRecruitKeywordFromGptAnswerJob(String name) {
        for (RecruitKeyword recruitKeyword : RecruitKeyword.values()) {
            if (recruitKeyword.toString().equals(name)) {
                return recruitKeyword;
            }
        }
        return null; // 일치하는거 없으면 null
    }

    public static RecruitKeyword getRecruitKeywordFromProfileJob(Job job) {
        return switch (job.getName()) {
            case "백엔드 개발자" -> BACKEND;
            case "프론트엔드 개발자" -> FRONTEND;
            case "PM(Product/Project Manager)" -> PM;
            case "Designer" -> DESIGNER;
            default -> throw new RuntimeException("추가 필요한 직무" + job.getName());
        };
    }


    public abstract List<String> getIncludeTitleKeywordList();
    public abstract List<String> getExcludeTitleKeywordList();
    public abstract List<String> getIncludeHashtagKeywordList();
}
