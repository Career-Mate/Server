package UMC.career_mate.global.security.oauth2;

import UMC.career_mate.domain.member.Member;
import UMC.career_mate.global.security.dto.user.CustomOAuth2User;
import UMC.career_mate.global.security.service.RefreshTokenService;
import UMC.career_mate.global.security.util.CookieUtil;
import UMC.career_mate.global.security.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    private static final String FRONTEND_BASE_URL = "http://localhost:3000";
    private static final String FRONTEND_PROFILE_PATH = "/profile";

    @Value("${spring.jwt.access-token-validity-in-seconds}")
    private Integer ACCESS_TOKEN_VALIDITY_IN_SECONDS;
    @Value("${spring.jwt.refresh-token-validity-in-seconds}")
    private Integer REFRESH_TOKEN_VALIDITY_IN_SECONDS;

    private static final String ACCESS_TOKEN = "access-token";
    private static final String REFRESH_TOKEN = "refresh-token";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        Member member = customOAuth2User.getMember();

        String accessToken = jwtUtil.createJwt(member.getId(), member.getClientId(), member.getSocialType(), true);
        CookieUtil.addCookie(response, ACCESS_TOKEN, accessToken, ACCESS_TOKEN_VALIDITY_IN_SECONDS);

        String refreshToken = jwtUtil.createJwt(member.getId(), member.getClientId(), member.getSocialType(), false);
        refreshTokenService.saveRefreshToken(member.getId(),refreshToken);
        CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken, REFRESH_TOKEN_VALIDITY_IN_SECONDS);

        if (!member.getIs_complete()) {
            //추가 정보가 입력되어 있지 않을 경우
            response.sendRedirect(FRONTEND_BASE_URL);

        } else {
            // 추가 정보가 입력된 경우
            response.sendRedirect(FRONTEND_BASE_URL + FRONTEND_PROFILE_PATH);
        }
    }

}
