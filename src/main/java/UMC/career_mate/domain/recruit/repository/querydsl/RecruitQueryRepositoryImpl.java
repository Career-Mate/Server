package UMC.career_mate.domain.recruit.repository.querydsl;

import static UMC.career_mate.domain.recruit.QRecruit.*;

import UMC.career_mate.domain.recruit.Recruit;
import UMC.career_mate.domain.recruit.enums.EducationLevel;
import UMC.career_mate.domain.recruit.enums.RecruitKeyword;
import UMC.career_mate.domain.recruit.enums.RecruitSortType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RecruitQueryRepositoryImpl implements RecruitQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Recruit> findByKeywordWithBooleanBuilder(RecruitKeyword recruitKeyword,
        EducationLevel educationLevel, Integer careerYear, RecruitSortType recruitSortType,
        Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        // includeTitleKeywordList에 있는 키워드 중 하나라도 포함되어야 함
        filterIncludeTitleKeywordList(recruitKeyword.getIncludeTitleKeywordList(), builder);

        // excludeTitleKeywordList에 있는 키워드가 포함되지 않아야 함
        if (Objects.nonNull(recruitKeyword.getExcludeTitleKeywordList())) {
            filterExcludeTitleKeywordList(recruitKeyword.getExcludeTitleKeywordList(), builder);
        }

        // includeHashtagKeywordList에 있는 키워드 중 하나라도 포함되어야 함
        if (Objects.nonNull(recruitKeyword.getIncludeHashtagKeywordList())) {
            filterIncludeHashtagKeywordList(recruitKeyword.getIncludeHashtagKeywordList(), builder);
        }

        // 학력 필터링
        filterEducation(educationLevel, builder);

        // 경력 필터링
        filterExperience(careerYear, builder);

        // 마감된 공고 필터링
        filterDeadLine(builder);

        List<Recruit> result = queryFactory
            .selectFrom(recruit)
            .where(builder)
            .orderBy(createOrderSpecifier(recruitSortType))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = queryFactory
            .select(recruit.count())
            .from(recruit)
            .where(builder)
            .fetchOne();

        return new PageImpl<>(result, pageable, total);
    }

    private void filterIncludeTitleKeywordList(List<String> includekeywordList, BooleanBuilder builder) {
        BooleanBuilder includeTitleBuilder = new BooleanBuilder();
        for (String keyword : includekeywordList) {
            if (isEnglish(keyword)) {
                includeTitleBuilder.or(recruit.title.containsIgnoreCase(keyword));
            } else {
                includeTitleBuilder.or(recruit.title.contains(keyword));
            }
        }
        builder.and(includeTitleBuilder);
    }

    private void filterExcludeTitleKeywordList(List<String> excludeKeywordList, BooleanBuilder builder) {
        BooleanBuilder excludeTitleBuilder = new BooleanBuilder();
        for (String keyword : excludeKeywordList) {
            if (isEnglish(keyword)) {
                excludeTitleBuilder.or(recruit.title.containsIgnoreCase(keyword));
            } else {
                excludeTitleBuilder.or(recruit.title.contains(keyword));
            }
        }
        builder.and(excludeTitleBuilder.not());
    }

    private void filterIncludeHashtagKeywordList(List<String> includeHashTagKeywordList,
        BooleanBuilder builder) {
        BooleanBuilder includeHashtagBuilder = new BooleanBuilder();
        for (String keyword : includeHashTagKeywordList) {
            if (isEnglish(keyword)) {
                includeHashtagBuilder.or(recruit.hashtags.containsIgnoreCase(keyword));
            } else {
                includeHashtagBuilder.or(recruit.hashtags.contains(keyword));
            }
        }

        builder.and(includeHashtagBuilder);
    }

    private void filterEducation(EducationLevel educationLevel, BooleanBuilder builder) {
        BooleanBuilder educationBuilder = new BooleanBuilder();
        educationBuilder.and(recruit.educationLevelCode.loe(educationLevel.getCode()));
        builder.and(educationBuilder);
    }

    private void filterExperience(Integer careerYear, BooleanBuilder builder) {
        BooleanBuilder experienceBuilder = new BooleanBuilder();
        // min = 0, max = 0 --> 경력 무관, 신입
        experienceBuilder.and(
            recruit.experienceLevelMin.eq(0).and(recruit.experienceLevelMax.eq(0)));

        // 위 조건이 참 "이거나"
        // 아래 조건이 참 인경우 모두 조회한다

        // 내 경력 년차 >= min and (내 경력 년차 <= max or max = 0)
        experienceBuilder.or(recruit.experienceLevelMin.loe(careerYear)
            .and(recruit.experienceLevelMax.goe(careerYear).or(recruit.experienceLevelMax.eq(0))));

        builder.and(experienceBuilder);
    }

    private void filterDeadLine(BooleanBuilder builder) {
        builder.and(recruit.deadLine.after(Expressions.constant(LocalDateTime.now())));
    }

    private boolean isEnglish(String keyword) {
        return keyword.matches("[a-zA-Z]+");
    }

    private OrderSpecifier createOrderSpecifier(RecruitSortType recruitSortType) {
        return switch (recruitSortType) {
            case DEADLINE_ASC -> new OrderSpecifier(Order.ASC, recruit.deadLine);
            case DEADLINE_DESC -> new OrderSpecifier(Order.DESC, recruit.deadLine);
            case POSTING_DESC -> new OrderSpecifier(Order.DESC, recruit.postingDate);
        };
    }
}
