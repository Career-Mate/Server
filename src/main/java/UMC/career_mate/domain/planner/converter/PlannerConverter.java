package UMC.career_mate.domain.planner.converter;

import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.planner.Planner;
import UMC.career_mate.domain.planner.dto.request.CreatePlannerDTO;

public class PlannerConverter {

    public static Planner toPlanner(Member member, CreatePlannerDTO createPlannerDTO){
            return Planner
                    .builder()
                    .activityName(createPlannerDTO.activityName())
                    .startTime(createPlannerDTO.startTime())
                    .endTime(createPlannerDTO.endTime())
                    .specific(createPlannerDTO.specific())
                    .measurable(createPlannerDTO.measurable())
                    .achievable(createPlannerDTO.achievable())
                    .relevant(createPlannerDTO.relevant())
                    .timeBound(createPlannerDTO.timeBound())
                    .otherPlans(createPlannerDTO.otherPlans())
                    .member(member)
                    .build();
    }
}
