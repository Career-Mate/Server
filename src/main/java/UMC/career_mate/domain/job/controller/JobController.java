package UMC.career_mate.domain.job.controller;

import UMC.career_mate.domain.job.Job;
import UMC.career_mate.domain.job.Service.JobService;
import UMC.career_mate.global.response.ApiResponse;
import UMC.career_mate.global.response.result.code.CommonResultCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "직무 API", description = "직무 도메인의 API 입니다.")
@RequestMapping("/job")
public class JobController {

    private final JobService jobService;

    @GetMapping
    @Operation(summary = "모든 직무 조회 API")
    public ApiResponse<List<Job>> getJobList() {
        List<Job> jobList = jobService.getJobList();
        return ApiResponse.onSuccess(CommonResultCode.GET_JOB_LIST, jobList);
    }
}
