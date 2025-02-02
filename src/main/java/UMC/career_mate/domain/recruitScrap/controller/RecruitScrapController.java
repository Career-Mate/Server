package UMC.career_mate.domain.recruitScrap.controller;

import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.recruitScrap.dto.response.RecruitScrapResponseDTO;
import UMC.career_mate.domain.recruitScrap.service.RecruitScrapCommandService;
import UMC.career_mate.domain.recruitScrap.service.RecruitScrapQueryService;
import UMC.career_mate.global.annotation.LoginMember;
import UMC.career_mate.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "채용 공고 스크랩 API", description = "채용 공고 스크랩 도메인의 API 입니다.")
@RequestMapping("/scrap/recruits")
public class RecruitScrapController {

    private final RecruitScrapCommandService recruitScrapCommandService;
    private final RecruitScrapQueryService recruitScrapQueryService;

    @Operation(
        summary = "채용 공고 스크랩 API",
        description = """
            채용 공고를 스크랩하는 API입니다.\n\n
            recruitId : 스크랩하려는 채용 공고 pk 값
            """)
    @PostMapping("/{recruitId}")
    public ApiResponse<String> createRecruitScrap(@LoginMember Member member,
        @PathVariable Long recruitId) {
        recruitScrapCommandService.saveRecruitScrap(member, recruitId);
        return ApiResponse.onSuccess("스크랩 완료");
    }

    @Operation(
        summary = "채용 공고 스크랩 삭제 API",
        description = """
            스크랩을 삭제하는 API입니다.\n\n
            recruitId : 삭제하려는 스크랩의 채용 공고 pk 값
            """)
    @DeleteMapping("/{recruitId}")
    public ApiResponse<String> deleteRecruitScrap(@LoginMember Member member,
        @PathVariable Long recruitId) {
        recruitScrapCommandService.deleteRecruitScrap(member, recruitId);
        return ApiResponse.onSuccess("스크랩 삭제 완료");
    }

    @Operation(
        summary = "채용 공고 스크랩 목록 조회 API",
        description = """
            스크랩한 채용 공고 목록을 조회하는 API입니다.\n\n
            로그인 인증만을 요구합니다.
            """)
    @GetMapping
    public ApiResponse<List<RecruitScrapResponseDTO>> getRecruitScrapList(
        @LoginMember Member member) {
        return ApiResponse.onSuccess(recruitScrapQueryService.findRecruitScrapList(member));
    }

}
