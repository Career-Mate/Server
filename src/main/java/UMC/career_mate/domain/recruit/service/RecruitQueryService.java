package UMC.career_mate.domain.recruit.service;

import UMC.career_mate.domain.recruit.Recruit;
import UMC.career_mate.domain.recruit.converter.RecruitConverter;
import UMC.career_mate.domain.recruit.dto.response.RecommendRecruitDTO;
import UMC.career_mate.domain.recruit.enums.EducationLevel;
import UMC.career_mate.domain.recruit.enums.RecruitKeyword;
import UMC.career_mate.domain.recruit.enums.RecruitSortType;
import UMC.career_mate.domain.recruit.repository.RecruitRepository;
import UMC.career_mate.global.common.PageResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitQueryService {

    private final RecruitRepository recruitRepository;

    public PageResponseDto<List<RecommendRecruitDTO>> getRecommendRecruitList(int page, int size,
        RecruitKeyword recruitKeyword, EducationLevel educationLevel,
        Integer careerYear, RecruitSortType recruitSortType) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);

        Page<Recruit> findRecruitPage = recruitRepository.findByKeywordWithBooleanBuilder(
            recruitKeyword, educationLevel, careerYear, recruitSortType, pageRequest);

        boolean hasNext = findRecruitPage.getSize() == size + 1;

        List<RecommendRecruitDTO> recommendRecruitDTOList = findRecruitPage.stream()
            .limit(size)
            .map(recruit -> RecruitConverter.toRecommendRecruitDTO(recruit))
            .toList();

        return PageResponseDto.<List<RecommendRecruitDTO>>builder()
            .page(page)
            .hasNext(hasNext)
            .result(recommendRecruitDTOList)
            .build();
    }
}
