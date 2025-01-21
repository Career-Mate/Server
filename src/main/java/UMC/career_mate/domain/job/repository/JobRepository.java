package UMC.career_mate.domain.job.repository;

import UMC.career_mate.domain.job.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}
