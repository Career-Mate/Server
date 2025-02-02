package UMC.career_mate.domain.planner.converter;

import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.planner.Planner;
import UMC.career_mate.domain.planner.dto.request.CreatePlannerDTO;
import UMC.career_mate.domain.planner.dto.response.PlannerListResponseDTO;
import UMC.career_mate.domain.planner.dto.response.PlannerResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlannerConverter {

    public static Planner toPlanner(Member member, CreatePlannerDTO createPlannerDTO){
            return Planner
                    .builder()
                    .activityName(createPlannerDTO.activityName())
                    .startTime(createPlannerDTO.startTime())
                    .endTime(createPlannerDTO.endTime())
                    .specifics(createPlannerDTO.specifics())
                    .measurable(createPlannerDTO.measurable())
                    .achievable(createPlannerDTO.achievable())
                    .relevant(createPlannerDTO.relevant())
                    .timeBound(createPlannerDTO.timeBound())
                    .otherPlans(createPlannerDTO.otherPlans())
                    .member(member)
                    .build();
    }

    public static List<Planner> toPlannerList(Member member, List<CreatePlannerDTO> createPlannerDTOList){
        return createPlannerDTOList.stream().map(createPlannerDTO -> PlannerConverter.toPlanner(member, createPlannerDTO)).toList();
    }

    public static PlannerResponseDTO toPlannerResponseDTO(Planner planner){
        return PlannerResponseDTO.builder()
                .activityName(planner.getActivityName())
                .startTime(planner.getStartTime())
                .endTime(planner.getEndTime())
                .specifics(planner.getSpecifics())
                .measurable(planner.getMeasurable())
                .achievable(planner.getAchievable())
                .relevant(planner.getRelevant())
                .timeBound(planner.getTimeBound())
                .otherPlans(planner.getOtherPlans())
                .build();
    }

    public static PlannerListResponseDTO toPlannerListResponseDTO(List<Planner> plannerList){
        List<PlannerResponseDTO> plannerResponseDTOList = plannerList.stream()
                                                            .map(PlannerConverter::toPlannerResponseDTO)
                                                            .toList();
        return PlannerListResponseDTO.builder()
                .planners(plannerResponseDTOList)
                .build();
    }

}
