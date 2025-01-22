package UMC.career_mate.domain.member.converter;

import UMC.career_mate.domain.member.Member;
import UMC.career_mate.domain.member.enums.SocialType;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter {

    public static Member toEmptyEntity(String clientId, SocialType socialType) {
        return Member.builder()
                .clientId(clientId)
                .socialType(socialType)
                .is_complete(false)
                .build();
    }
}
