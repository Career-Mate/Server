package UMC.career_mate.global.security;

import UMC.career_mate.global.response.ApiResponse;
import UMC.career_mate.global.security.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class SecurityController {

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/refresh")
    public ApiResponse<String> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        refreshTokenService.refreshAccessToken(request, response);
        return ApiResponse.onSuccess("access token 재발급에 성공하였습니다.");
    }
}
