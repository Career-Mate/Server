package UMC.career_mate.domain.member.controller;

import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.member.dto.request.JoinMemberDTO;
import UMC.career_mate.domain.member.dto.response.MemberInfoDTO;
import UMC.career_mate.domain.member.service.MemberService;
import UMC.career_mate.global.annotation.LoginMember;
import UMC.career_mate.global.response.ApiResponse;
import UMC.career_mate.global.response.result.code.CommonResultCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    @Operation(summary = "회원가입 API",
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
                    """
    )
    public ApiResponse<String> joinMember(JoinMemberDTO request) {
        Member member = memberService.joinMember(request);
        return ApiResponse.onSuccess("프로필 설정 완료");
    }

    @GetMapping
    @Operation(summary = "회원 정보 조회 API")
    public ApiResponse<MemberInfoDTO> getMemberInfo(@LoginMember Member member) {
        MemberInfoDTO memberInfo = memberService.getMemberInfo(member);

        return ApiResponse.onSuccess(CommonResultCode.MEMBER_INFO, memberInfo);
    }
}
