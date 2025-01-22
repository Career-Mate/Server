package UMC.career_mate.domain.job.Service;

import UMC.career_mate.domain.job.Job;
import UMC.career_mate.domain.job.repository.JobRepository;
import UMC.career_mate.global.response.exception.GeneralException;
import UMC.career_mate.global.response.exception.code.CommonErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

    public Job findJobById(Long id) {
        return jobRepository.findById(id).orElseThrow(
                () -> new GeneralException(CommonErrorCode.NOT_FOUND_BY_JOB_ID)
        );
    }

    public List<Job> getJobList() {
        return jobRepository.findAll();
    }
}
