package UMC.career_mate.domain.member.controller;

import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.member.dto.request.CreateProfileDTO;
import UMC.career_mate.domain.member.dto.response.MemberInfoDTO;
import UMC.career_mate.domain.member.service.MemberService;
import UMC.career_mate.global.annotation.LoginMember;
import UMC.career_mate.global.response.ApiResponse;
import UMC.career_mate.global.response.result.code.CommonResultCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "회원 API", description = "회원 도메인의 API 입니다.")
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/profile")
    @Operation(summary = "프로필 설정 API",
            description =
                    """
                    아래 두가지 요소에는 반드시 목록 중 하나를 입력해주세요
                    educationLevel(학력) 에 들어가야 할 목록입니다.
                    1. MIDDLE (중학교 이하) \s
                    2. HIGH (고등학교) \s
                    3. JUNIOR_COLLEGE (전문대학) \s
                    4. UNIVERSITY (대학교) \s
                    5. MASTER (석사) \s
                    6. DOCTOR (박사) \s

                    educationStatus(수료 상태)에 들어가야 할 목록입니다.
                    1. ENROLLED (재학) \s
                    2. ON_LEAVE (휴학) \s
                    3. GRADUATED (졸업) \s
                    4. COMPLETED (수료) \s
                    
                    직무는 직무 id를 넣어주세요.\s
                    ex) "job" : 1
                    """
    )
    public ApiResponse<String> createProfile(@RequestBody CreateProfileDTO request,
                                             @LoginMember Member member)
    {
        Member profile = memberService.makeProfile(request, member);
        return ApiResponse.onSuccess("프로필 설정 완료");
    }

    @GetMapping
    @Operation(summary = "회원 정보 조회 API")
    public ApiResponse<MemberInfoDTO> getMemberInfo(@LoginMember Member member) {
        MemberInfoDTO memberInfo = memberService.getMemberInfo(member);

        return ApiResponse.onSuccess(CommonResultCode.MEMBER_INFO, memberInfo);
    }

    @PatchMapping("/modify")
    @Operation(summary = "회원 정보 수정 API",
            description =
                    """
                    아래 두가지 요소에는 반드시 목록 중 하나를 입력해주세요
                    educationLevel(학력) 에 들어가야 할 목록입니다.
                    1. MIDDLE (중학교 이하) \s
                    2. HIGH (고등학교) \s
                    3. JUNIOR_COLLEGE (전문대학) \s
                    4. UNIVERSITY (대학교) \s
                    5. MASTER (석사) \s
                    6. DOCTOR (박사) \s
                    
                    educationStatus(수료 상태)에 들어가야 할 목록입니다.
                    1. ENROLLED (재학) \s
                    2. ON_LEAVE (휴학) \s
                    3. GRADUATED (졸업) \s
                    4. COMPLETED (수료) \s
                    
                    직무는 직무 id를 넣어주세요.\s
                    ex) "job" : 1
                  
                    """)
    public ApiResponse<MemberInfoDTO> changeProfile(@RequestBody CreateProfileDTO request,
                                                    @LoginMember Member member) {
        Member profile = memberService.changeProfile(member.getId(), request);
        MemberInfoDTO memberInfo = memberService.getMemberInfo(profile);

        return ApiResponse.onSuccess(CommonResultCode.MODIFY_MEMBER_INFO, memberInfo);
    }
}
