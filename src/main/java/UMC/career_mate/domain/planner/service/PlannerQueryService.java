package UMC.career_mate.domain.planner.service;

import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.planner.Planner;
import UMC.career_mate.domain.planner.converter.PlannerConverter;
import UMC.career_mate.domain.planner.dto.response.PlannerResponseDTO;
import UMC.career_mate.domain.planner.repository.PlannerRepository;
import UMC.career_mate.global.response.exception.GeneralException;
import UMC.career_mate.global.response.exception.code.CommonErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlannerQueryService {

    private final PlannerRepository plannerRepository;

    public PlannerResponseDTO getPlannerByMember(Member member) {
        Planner planner = plannerRepository.findPlannerByMember(member).orElseThrow(
                ()-> new GeneralException(CommonErrorCode.PLANNER_NOT_EXISTS)
        );

        return PlannerConverter.toPlannerResponseDTO(planner);
    }
}
