package UMC.career_mate.global.security.dto.user;

import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.member.enums.SocialType;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
public class CustomOAuth2User implements OAuth2User {

    private final Member member;

    public CustomOAuth2User(Member member) {
        this.member = member;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getName() {
        // getName이 null을 반환하면 안되기 때문에 clientId로 반환
        return member.getClientId();
    }

    public SocialType getSocialType() {
        return member.getSocialType();
    }

    public String getClientId() {
        return member.getClientId();
    }

    public Long getMemberId() {
        return member.getId();
    }

    public Boolean getProfileStatus() {
        return member.getIs_complete();
    }
}
