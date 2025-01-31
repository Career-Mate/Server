package UMC.career_mate.domain.planner;

import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.planner.dto.request.CreatePlannerDTO;
import UMC.career_mate.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "planners")
@SQLRestriction("deleted_at is NULL")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Planner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "planner_id")
    private Long id;

    @Column(name = "activity_name")
    private String activityName;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column
    private String specifics;

    @Column
    private String measurable;

    @Column
    private String achievable;

    @Column
    private String relevant;

    @Column(name = "time_bound")
    private String timeBound;

    @Column(name = "other_plans")
    private String otherPlans;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void update(CreatePlannerDTO createPlannerDTO){
        this.activityName = createPlannerDTO.activityName();
        this.startTime = createPlannerDTO.startTime();
        this.endTime = createPlannerDTO.endTime();
        this.specifics = createPlannerDTO.specifics();
        this.measurable = createPlannerDTO.measurable();
        this.achievable = createPlannerDTO.achievable();
        this.relevant = createPlannerDTO.relevant();
        this.timeBound = createPlannerDTO.timeBound();
        this.otherPlans = createPlannerDTO.otherPlans();
    }
}
