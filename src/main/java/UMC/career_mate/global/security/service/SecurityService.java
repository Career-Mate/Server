package UMC.career_mate.global.security.service;

import UMC.career_mate.global.response.exception.GeneralException;
import UMC.career_mate.global.response.exception.code.CommonErrorCode;
import UMC.career_mate.global.security.jwt.repository.RefreshTokenRepository;
import UMC.career_mate.global.security.util.CookieUtil;
import UMC.career_mate.global.security.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository repository;

    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken = CookieUtil.getCookieValue(request, response, "refresh-token");

        if (refreshToken != null) {
            //redis에서 리프레시 토큰 삭제
            Long memberId = jwtUtil.getMemberId(refreshToken);
            repository.deleteById(memberId);
        } else {
//            response.sendRedirect("https://www.careermate.site?status=f");
            throw new GeneralException(CommonErrorCode.INVALID_TOKEN);
        }

        //쿠키에서 토큰 삭제
        CookieUtil.deleteCookie(request, response, "access-token");
        CookieUtil.deleteCookie(request, response, "refresh-token");
    }
}
