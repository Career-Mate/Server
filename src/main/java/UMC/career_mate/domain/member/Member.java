package UMC.career_mate.domain.member;

import UMC.career_mate.domain.job.Job;
import UMC.career_mate.domain.member.enums.EducationStatus;
import UMC.career_mate.domain.member.enums.MemberEducationLevel;
import UMC.career_mate.domain.member.enums.SocialType;
import UMC.career_mate.domain.recruit.enums.EducationLevel;
import UMC.career_mate.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "members")
@SQLRestriction("deleted_at is NULL")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    private String email;

    @Column(name = "education_level")
    @Enumerated(EnumType.STRING)
    private MemberEducationLevel educationLevel;

    @Column(name = "education_status")
    @Enumerated(EnumType.STRING)
    private EducationStatus educationStatus;

    @Column(name = "social_type")
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(name = "client_id")
    private String clientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    //profile 작성이 되었는지 확인하는 필드
    private Boolean is_complete;
}
