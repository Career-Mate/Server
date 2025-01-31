package UMC.career_mate.global.security.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.util.WebUtils;

import java.io.Serializable;
import java.util.Base64;

public class CookieUtil {

    private static final String COOKIE_DOMAIN = "54.180.29.116";

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .domain(COOKIE_DOMAIN)
                .maxAge(maxAge)
                .httpOnly(true)
//                .secure(true)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public static void addCookieNoAge(HttpServletResponse response, String name, String value) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .domain(COOKIE_DOMAIN)
                .httpOnly(true)
//                .secure(true)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public static void addCookieLocalHost(HttpServletResponse response, String name, String value, int maxAge) {

        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .domain("localhost")
                .maxAge(maxAge)
                .httpOnly(true)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public static String getCookieValue(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        return (cookie != null) ? cookie.getValue() : null;
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie getCookie = WebUtils.getCookie(request, name);
        if (getCookie != null) {
            Cookie cookie = new Cookie(getCookie.getName(), null);
            cookie.setPath("/");
            cookie.setDomain(COOKIE_DOMAIN);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    public static String serialize(Object obj) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize((Serializable) obj));
    }

    // 쿠키를 역직렬화(String -> Object)해 객체로 변환
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
    }
}
