package UMC.career_mate.global.security.dto.response;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {
    private final Map<String,Object> attribute;

    public KakaoResponse(Map<String,Object> attribute){
        this.attribute = attribute;
    }

    @Override
    public String getProvider() {
        return "KAKAO";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }
}
