package UMC.career_mate.domain.content.controller;

import UMC.career_mate.domain.content.dto.request.ContentRequestDTO;
import UMC.career_mate.domain.content.dto.response.ContentResponseDTO;
import UMC.career_mate.domain.content.service.ContentService;
import UMC.career_mate.domain.contentScrap.dto.response.ContentScrapResponseDTO;
import UMC.career_mate.domain.contentScrap.service.ContentScrapService;
import UMC.career_mate.domain.member.Member;
import UMC.career_mate.global.annotation.LoginMember;
import UMC.career_mate.global.common.PageResponseDTO;
import UMC.career_mate.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/content")
public class ContentController {

    private final ContentService contentService;
    private final ContentScrapService contentScrapService;

    @PostMapping
    @Operation(
            summary = "컨텐츠 업로드 API",
            description = """
                    새로운 컨텐츠를 생성합니다. (관리자용)
                    ### 요청 예시 JSON:
                    ```json
                    {
                        "title": "Spring Boot Guide",
                        "url": "https://example.com/spring-boot",
                        "photo": "https://example.com/image.jpg",
                        "jobId": 1
                    }
                    ```
                    """
    )
    public ApiResponse<ContentResponseDTO> uploadContent(@RequestBody ContentRequestDTO contentRequestDTO) {
        return ApiResponse.onSuccess(contentService.uploadContent(contentRequestDTO));
    }

    @GetMapping
    @Operation(
            summary = "직무별 컨텐츠 조회 API",
            description = """
                    특정 직무 ID에 해당하는 컨텐츠를 조회합니다.
                    Query Parameters:
                    - `jobId`: 직무 ID (필수)
                    - `page`: 페이지 번호 (기본값: 1)
                    - `size`: 페이지 크기 (기본값: 10)
                    """
    )
    public ApiResponse<PageResponseDTO<List<ContentResponseDTO>>> getContentsByJobId(
            @RequestParam Long jobId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.onSuccess(contentService.getContentsByJobId(jobId, page, size));
    }

    @PostMapping("/{contentId}/scrap")
    @Operation(
            summary = "컨텐츠 스크랩 API",
            description = """
                    컨텐츠를 스크랩합니다. 
                    Path Parameters:
                    - 'contentId': 스크랩할 콘텐츠 ID
                    """
    )
    public ApiResponse<String> createScrapContents(@LoginMember Member member, @PathVariable Long contentId) {
        contentScrapService.createScrapContents(member, contentId);
        return ApiResponse.onSuccess("컨텐츠 스크랩 완료");
    }

    @DeleteMapping("/{contentId}/scrap")
    @Operation(
            summary = "컨텐츠 스크랩 삭제 API",
            description = """
                컨텐츠의 스크랩을 삭제합니다.
                Path Parameters:
                - 'contentId': 삭제할 콘텐츠 ID
                """
    )
    public ApiResponse<String> deleteScrapContents(@PathVariable Long contentId, @LoginMember Member member) {
        contentScrapService.deleteScrapContents(contentId, member);
        return ApiResponse.onSuccess("컨텐츠 스크랩 삭제 완료");
    }

    @GetMapping("/scrap")
    @Operation(
            summary = "스크랩한 컨텐츠 조회 API",
            description = """
                사용자가 스크랩한 컨텐츠를 조회합니다.
                Query Parameters:
                - 'page': 페이지 번호 (기본값: 1)
                - 'size': 페이지 크기 (기본값: 10)
                """
    )
    public ApiResponse<PageResponseDTO<List<ContentScrapResponseDTO>>> getScrapContents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @LoginMember Member member) {
        return ApiResponse.onSuccess(contentScrapService.getScrapContents(member, page, size));
    }
}