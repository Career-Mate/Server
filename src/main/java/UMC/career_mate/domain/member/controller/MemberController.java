package UMC.career_mate.domain.member.controller;

import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.member.dto.request.JoinMemberDTO;
import UMC.career_mate.domain.member.service.MemberService;
import UMC.career_mate.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ApiResponse<String> joinMember(JoinMemberDTO request) {
        Member member = memberService.joinMember(request);

        return ApiResponse.onSuccess("프로필 설정 완료");
    }
}
