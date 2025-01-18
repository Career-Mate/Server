package UMC.career_mate.global.scheduler;

import UMC.career_mate.domain.recruit.enums.JobCode;
import UMC.career_mate.domain.recruit.service.RecruitCommandService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecruitScheduler {

    private final RecruitCommandService recruitCommandService;

    @Scheduled(cron = "0 0 6 * * *")
    public void saveRecruitInfo() {
        List<JobCode> jobCodeList = JobCode.getTrueSearchTarget();

        for (JobCode jobCode : jobCodeList) {
            log.info("JobCode : {}, 호출", jobCode.getName());
            recruitCommandService.saveRecruitInfoOfSaramin(jobCode);
            log.info("JobCode : {}, 종료", jobCode.getName());
        }
    }
}
