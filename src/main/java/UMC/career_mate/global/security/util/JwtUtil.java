package UMC.career_mate.global.security.util;

import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.member.enums.SocialType;
import UMC.career_mate.domain.member.service.MemberService;
import UMC.career_mate.global.response.exception.GeneralException;
import UMC.career_mate.global.response.exception.code.CommonErrorCode;
import UMC.career_mate.global.security.dto.user.CustomOAuth2User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtUtil {

    private SecretKey secretKey;
    private final MemberService memberService;

    @Value("${spring.jwt.access-token-validity-in-seconds}")
    private Long ACCESS_TOKEN_VALIDITY_IN_SECONDS;
    @Value("${spring.jwt.refresh-token-validity-in-seconds}")
    private Long REFRESH_TOKEN_VALIDITY_IN_SECONDS;

    private static final String HASH_ALGORITHM = Jwts.SIG.HS256.key().build().getAlgorithm();
    private static final String PAYLOAD_MEMBER_ID_KEY = "memberId";
    private static final String PAYLOAD_CLIENT_ID_KEY = "clientId";
    private static final String PAYLOAD_SOCIAL_TYPE = "socialType";


    public JwtUtil(@Value("${spring.jwt.secret}") String secret, MemberService memberService){
        this.memberService  = memberService;
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HASH_ALGORITHM);
    }

    public String createJwt(Long memberId, String clientId, SocialType socialType, Boolean isAccess) {
        final LocalDateTime now = LocalDateTime.now();
        final Date issuedDate = localDateTimeToDate(now);
        final Date expiredDate;
        if (isAccess) {
            expiredDate = localDateTimeToDate(now.plusSeconds(ACCESS_TOKEN_VALIDITY_IN_SECONDS));
        } else {
            expiredDate = localDateTimeToDate(now.plusSeconds(REFRESH_TOKEN_VALIDITY_IN_SECONDS));
        }

        return Jwts.builder()
                .claim(PAYLOAD_MEMBER_ID_KEY, memberId.toString())
                .claim(PAYLOAD_CLIENT_ID_KEY, clientId)
                .claim(PAYLOAD_SOCIAL_TYPE, socialType.toString())
                .issuedAt(issuedDate)
                .expiration(expiredDate)
                .signWith(secretKey)
                .compact();
    }

    public Claims getPayload(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (GeneralException e) {
            throw new GeneralException(CommonErrorCode.INVALID_TOKEN);
        }
    }

    public String getClientId(String token) {
        return getPayload(token).get(PAYLOAD_CLIENT_ID_KEY, String.class);
    }

    public Long getMemberId(String token) {
        return Long.parseLong(getPayload(token).get(PAYLOAD_MEMBER_ID_KEY, String.class));
    }

    public SocialType getSocialType(String token) {
        return SocialType.valueOf(getPayload(token).get(PAYLOAD_SOCIAL_TYPE, String.class));
    }

    public Boolean isExpired(String token) {
        return getPayload(token).getExpiration().before(new Date());
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Long memberId = getMemberId(token);
        String clientId = getClientId(token);
        Member member = memberService.findMemberByIdAndClientId(memberId, clientId);

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(member);
        return new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
    }


    private Date localDateTimeToDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }
}
