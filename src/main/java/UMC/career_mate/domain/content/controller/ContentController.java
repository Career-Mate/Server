package UMC.career_mate.domain.content.controller;

import UMC.career_mate.domain.content.dto.request.ContentRequestDTO;
import UMC.career_mate.domain.content.dto.response.ContentResponseDTO;
import UMC.career_mate.domain.content.service.ContentService;
import UMC.career_mate.domain.job.Service.JobService;
import UMC.career_mate.domain.member.Member;
import UMC.career_mate.global.annotation.LoginMember;
import UMC.career_mate.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/content")
public class ContentController {

    private final JobService jobService;
    private final ContentService contentService;

    @PostMapping // 컨텐츠 업로드 (관리자용)
    @Operation(
            summary = "컨텐츠 업로드 API",
            description = """
            관리자가 api로 컨텐츠를 업로드할 때 사용합니다.
            요청 바디에 아래 필드를 포함해야 합니다.
            - `title`: 컨텐츠 제목 (필수)
            - `url`: 컨텐츠 링크 (필수)
            - `photo`: 썸네일 이미지 링크 (필수)
            - `jobId`: 직무 ID (필수)
            """
    )
    public ApiResponse<ContentResponseDTO> uploadContent(@RequestBody ContentRequestDTO contentRequestDTO) {
        return ApiResponse.onSuccess(contentService.uploadContent(contentRequestDTO));
    }

    @GetMapping // 직무별 콘텐츠 조회
    @Operation(
            summary = "직무별 컨텐츠 조회 API",
            description = """
        요청된 직무 ID에 따라 해당 직무의 콘텐츠를 조회합니다.
        Query Parameters를 통해 jobid를 받고, 만약 해당 jobid가 db에 존재하지 않는다면 EJB000, 400 에러코드를 띄웁니다.
        요청 파라미터:
        - `jobId`: 조회할 직무 ID (필수)
        - `page`: 페이지 번호 (기본값: 1)
        - `size`: 페이지 크기 (기본값: 10)
        """
    )
    public ApiResponse<?> getContentsByJobId(@RequestParam Long jobId,
                                             @RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        // 직무 ID 검증
        jobService.findJobById(jobId);

        // 콘텐츠 조회
        return ApiResponse.onSuccess(contentService.getContentsByJobId(jobId, page, size));
    }
}
