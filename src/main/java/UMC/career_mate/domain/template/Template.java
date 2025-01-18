package UMC.career_mate.domain.template;

import UMC.career_mate.domain.job.Job;
import UMC.career_mate.domain.template.enums.TemplateType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "templates")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    private Long id;

    @Column(name = "template_type")
    @Enumerated(EnumType.STRING)
    private TemplateType templateType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;
}
