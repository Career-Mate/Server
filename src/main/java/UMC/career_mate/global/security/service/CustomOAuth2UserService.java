package UMC.career_mate.global.security.service;

import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.member.enums.SocialType;
import UMC.career_mate.domain.member.service.MemberService;
import UMC.career_mate.global.response.exception.GeneralException;
import UMC.career_mate.global.response.exception.code.CommonErrorCode;
import UMC.career_mate.global.security.dto.response.KakaoResponse;
import UMC.career_mate.global.security.dto.response.NaverResponse;
import UMC.career_mate.global.security.dto.response.OAuth2Response;
import UMC.career_mate.global.security.dto.user.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else {
            throw new GeneralException(CommonErrorCode.INVALID_SOCIAL_PLATFORM);
        }

        SocialType socialType = SocialType.valueOf(oAuth2Response.getProvider());
        String clientId = oAuth2Response.getProviderId();

        if (!memberService.checkExistMember(clientId, socialType)) { // 서비스에 처음 로그인한 회원
            Member member = memberService.saveEmptyMember(clientId, socialType);
            return new CustomOAuth2User(member);
        } else { // 이미 가입이 되어있는 회원
            Member member = memberService.findMemberByClientIdAndSocialType(clientId, socialType);
            return new CustomOAuth2User(member);
        }
    }

}
