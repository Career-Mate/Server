package UMC.career_mate.domain.recruit.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecruitSortType {

    DEADLINE_ASC("deadLine", "asc"), // 마감일 빠른 순
    DEADLINE_DESC("deadLine", "desc"), // 마감일 늦은 순
    OPENING_ASC("openingDate", "asc"), // 원서 접수 시작일 빠른 순
    OPENING_DESC("openingDate", "desc"), // 원서 접수 시작일 늦은 순
    POSTING_ASC("postingDate", "asc"), // 사람인에 채용 공고 작성일 빠른 순
    POSTING_DESC("postingDate", "desc"); // 사람인에 채용 공고 작성일 늦은 순

    private final String field;
    private final String direction;
}
