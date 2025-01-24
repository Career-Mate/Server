package UMC.career_mate.global.security.service;

import UMC.career_mate.domain.member.enums.SocialType;
import UMC.career_mate.global.security.jwt.entity.RefreshToken;
import UMC.career_mate.global.response.exception.GeneralException;
import UMC.career_mate.global.response.exception.code.CommonErrorCode;
import UMC.career_mate.global.security.jwt.repository.RefreshTokenRepository;
import UMC.career_mate.global.security.util.CookieUtil;
import UMC.career_mate.global.security.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    public RefreshToken findByMemberId(Long memberId) {
        return refreshTokenRepository.findByMemberId(memberId).orElseThrow(
                () -> new GeneralException(CommonErrorCode.NOT_FOUND_REFRESH_TOKEN_BY_MEMBER_ID)
        );
    }

    @Transactional
    public void saveRefreshToken(Long memberId, String refreshToken) {
        RefreshToken updatedRefreshToken = refreshTokenRepository.findByMemberId(memberId)
                .map(originalRefreshToken -> originalRefreshToken.update(refreshToken))
                .orElse(new RefreshToken(memberId, refreshToken));
        //토큰을 저장할때 TTL을 초기화
        updatedRefreshToken.resetExpiration();

        refreshTokenRepository.save(updatedRefreshToken);
    }

    @Transactional
    public void refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = CookieUtil.getCookieValue(request, response, "refresh-token");

        if (refreshToken == null || jwtUtil.isExpired(refreshToken)) {
            CookieUtil.deleteCookie(request, response, "refresh-token");
            throw new GeneralException(CommonErrorCode.EXPIRED_TOKEN);
        }

        Long memberId = jwtUtil.getMemberId(refreshToken);
        RefreshToken token = findByMemberId(memberId);

        if (!refreshToken.equals(token.getRefreshToken())) {
            CookieUtil.deleteCookie(request, response, "refresh-token");
            throw new GeneralException(CommonErrorCode.INVALID_TOKEN);
        }

        String clientId = jwtUtil.getClientId(refreshToken);
        SocialType socialType = jwtUtil.getSocialType(refreshToken);

        String newAccessToken = jwtUtil.createJwt(memberId, clientId, socialType, true);
        String newRefreshToken = jwtUtil.createJwt(memberId, clientId, socialType, false);
        saveRefreshToken(memberId, newRefreshToken);

        CookieUtil.addCookie(response, "access-token", newAccessToken, -1);
        CookieUtil.addCookie(response, "refresh-token", newRefreshToken, 86400);
    }
}
