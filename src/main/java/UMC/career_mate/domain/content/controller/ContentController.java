package UMC.career_mate.domain.content.controller;

import UMC.career_mate.domain.content.dto.request.ContentRequestDTO;
import UMC.career_mate.domain.content.dto.response.ContentResponseDTO;
import UMC.career_mate.domain.content.service.ContentService;
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

    private final ContentService contentService;

    @PostMapping // 컨텐츠 업로드 (관리자용)
    @Operation(
            summary = "컨텐츠 업로드 API",
            description = """
            관리자가 직무에 맞는 컨텐츠를 업로드할 때 사용합니다.
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

    @GetMapping // 로그인한 사용자의 직무 기반 콘텐츠 조회
    @Operation(
            summary = "로그인 사용자의 직무별 콘텐츠 조회 API",
            description = """
            로그인된 사용자의 회원 정보에 있는 직무 ID를 바탕으로 관련 콘텐츠를 조회합니다.
            페이지네이션 지원. 인증된 사용자만 접근 가능.
            """
    )
    public ApiResponse<List<ContentResponseDTO>> getContentsByUserJob(
            @LoginMember Member member,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        // 로그인한 사용자의 직무 ID를 가져옵니다.
        Long jobId = member.getJob().getId();

        // 서비스 계층에서 콘텐츠 조회
        return ApiResponse.onSuccess(contentService.getContentsByJobId(jobId, page, size));
    }


}
