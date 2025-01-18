package UMC.career_mate.domain.planner.service;

import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.planner.Planner;
import UMC.career_mate.domain.planner.converter.PlannerConverter;
import UMC.career_mate.domain.planner.dto.request.CreatePlannerDTO;
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
@Transactional
public class PlannerCommandService {

    private final PlannerRepository plannerRepository;

    public void savePlanner(Member member, CreatePlannerDTO createPlannerDTO){
        if(plannerRepository.existsByMember(member)){
            throw new GeneralException(CommonErrorCode.PLANNER_EXISTS);
        }
        Planner planner = PlannerConverter.toPlanner(member, createPlannerDTO);
        plannerRepository.save(planner);
    }

    public void editPlanner(Member member, CreatePlannerDTO createPlannerDTO){
        Planner planner = plannerRepository.findPlannerByMember(member).orElseThrow(
                ()->new GeneralException(CommonErrorCode.PLANNER_NOT_EXISTS));

        planner.update(createPlannerDTO);
    }

    public void deletePlanner(Member member){
        Planner planner = plannerRepository.findPlannerByMember(member).orElseThrow(
                ()->new GeneralException(CommonErrorCode.PLANNER_NOT_EXISTS));
        plannerRepository.delete(planner);
    }

}
