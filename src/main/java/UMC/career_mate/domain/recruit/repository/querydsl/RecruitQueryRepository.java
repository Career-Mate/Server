package UMC.career_mate.domain.recruit.repository.querydsl;

import UMC.career_mate.domain.recruit.Recruit;
import UMC.career_mate.domain.recruit.enums.EducationLevel;
import UMC.career_mate.domain.recruit.enums.RecruitKeyword;
import UMC.career_mate.domain.recruit.enums.RecruitSortType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecruitQueryRepository {

    Page<Recruit> findByKeywordWithBooleanBuilder(RecruitKeyword recruitKeyword,
        EducationLevel educationLevel, Integer careerYear, RecruitSortType recruitSortType,
        Pageable pageable);
}
