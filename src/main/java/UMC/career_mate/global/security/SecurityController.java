package UMC.career_mate.global.security;

import UMC.career_mate.global.response.ApiResponse;
import UMC.career_mate.global.security.service.RefreshTokenService;
import UMC.career_mate.global.security.service.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "인증 API", description = "인증 도메인의 API 입니다.")
@RequestMapping("/auth")
public class SecurityController {

    private final RefreshTokenService refreshTokenService;
    private final SecurityService securityService;

    @PostMapping("/refresh")
    @Operation(summary = "access토큰을 재발급하는 API",
        description = "쿠키에 refresh-token이 들어있어야 합니다.\s" +
                "만약 에러코드가 4002/ETK001, 4003/ETK004 인 경우에 호출해주세요"
    )
    public ApiResponse<String> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        refreshTokenService.refreshAccessToken(request, response);
        return ApiResponse.onSuccess("access token 재발급에 성공하였습니다.");
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest request, HttpServletResponse response) {
        securityService.logout(request, response);
        return ApiResponse.onSuccess("Logout Success");
    }
}
