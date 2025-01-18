package UMC.career_mate.domain.planner;

import UMC.career_mate.domain.member.Member;
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

    @Column(name = "activity_name", nullable = false)
    private String activityName;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private String specific;

    @Column(nullable = false)
    private String measurable;

    @Column(nullable = false)
    private String achievable;

    @Column(nullable = false)
    private String relevant;

    @Column(name = "time_bound", nullable = false)
    private String timeBound;

    @Column(name = "other_plans", nullable = false)
    private String otherPlans;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
