package UMC.career_mate.domain.planner.service;

import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.planner.Planner;
import UMC.career_mate.domain.planner.converter.PlannerConverter;
import UMC.career_mate.domain.planner.dto.request.CreatePlannerDTO;
import UMC.career_mate.domain.planner.dto.request.CreatePlannerListDTO;
import UMC.career_mate.domain.planner.repository.PlannerRepository;
import UMC.career_mate.global.response.exception.GeneralException;
import UMC.career_mate.global.response.exception.code.CommonErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PlannerCommandService {

    private final PlannerRepository plannerRepository;

    public void savePlanner(Member member){

        if(plannerRepository.existsByMember(member)){
            throw new GeneralException(CommonErrorCode.PLANNER_EXISTS);
        }

        List<CreatePlannerDTO> createPlannerDTOList = List.of(
                CreatePlannerDTO.builder().build(),
                CreatePlannerDTO.builder().build()
        );

        plannerRepository.saveAll(PlannerConverter.toPlannerList(member, createPlannerDTOList));
    }

    public void editPlanner(Member member, CreatePlannerListDTO createPlannerListDTO){

        List<Planner> planners = plannerRepository.findByMember(member);
        List<CreatePlannerDTO> plannerDTOs = createPlannerListDTO.planners();

        Iterator<CreatePlannerDTO> dtoIterator = plannerDTOs.iterator();
        planners.forEach(planner -> planner.update(dtoIterator.next()));

    }

    public void deletePlanner(Member member){

        List<Planner> planners = plannerRepository.findByMember(member);
        plannerRepository.deleteAll(planners);

    }

}
