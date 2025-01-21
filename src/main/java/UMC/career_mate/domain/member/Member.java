package UMC.career_mate.domain.member;

import UMC.career_mate.domain.job.Job;
import UMC.career_mate.domain.member.enums.EducationStatus;
import UMC.career_mate.domain.member.enums.MemberEducationLevel;
import UMC.career_mate.domain.member.enums.SocialType;
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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(name = "education_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberEducationLevel educationLevel;

    @Column(name = "education_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EducationStatus educationStatus;

    @Column(name = "social_type")
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

}
