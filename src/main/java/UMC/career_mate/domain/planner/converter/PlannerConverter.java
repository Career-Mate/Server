package UMC.career_mate.domain.planner.converter;

import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.planner.Planner;
import UMC.career_mate.domain.planner.dto.request.CreatePlannerDTO;
import UMC.career_mate.domain.planner.dto.response.PlannerResponseDTO;

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
}
