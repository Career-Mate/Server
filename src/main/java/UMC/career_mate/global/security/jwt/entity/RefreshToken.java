package UMC.career_mate.global.security.jwt.entity;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;


import java.io.Serializable;

@RedisHash("refresh_token")
public class RefreshToken implements Serializable {

    @Id
    @Indexed
    private Long memberId;

    @Getter
    @Indexed
    private String refreshToken;

    @TimeToLive
    @Value("${spring.jwt.refresh-token-validity-in-seconds}")
    private Long expiration;

    public RefreshToken(final Long memberId, final String refreshToken) {
        this.memberId = memberId;
        this.refreshToken = refreshToken;
    }

    public RefreshToken update(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

}
