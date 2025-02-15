package UMC.career_mate.global.security.jwt.filter;

import UMC.career_mate.global.response.exception.ErrorCode;
import UMC.career_mate.global.response.exception.code.CommonErrorCode;
import UMC.career_mate.global.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final List<String> EXCLUDE_URL_LIST = List.of(
            "/swagger-ui",
            "/swagger-resources",
            "/v3/api-docs",
            "/auth");
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = null;
        String refresh_check = null;

        authorization = request.getHeader("Authorization");

        if (authorization != null) {
            final Authentication authentication = jwtUtil.getAuthentication(authorization);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
            return;
        }

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            PrintWriter writer = response.getWriter();
            writer.write("{\"status\": \"f\", \"message\": \"토큰이 없습니다. 로그인 필요.\"}");
            writer.flush();
//            response.sendRedirect("https://www.careermate.site/login?status=f"); //토큰이 없으면 Login화면으로 리다이렉트
//            filterChain.doFilter(request, response);
            return;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("access-token")) {
                authorization = cookie.getValue();
            } else if (cookie.getName().equals("refresh-token")) {
                refresh_check = cookie.getValue();
            }
        }

        if (authorization == null) {
            SecurityContextHolder.clearContext();
            if (refresh_check == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                PrintWriter writer = response.getWriter();
                writer.write("{\"status\": \"f\", \"message\": \"토큰이 없습니다. 로그인 필요.\"}");
                writer.flush();
//                response.sendRedirect("https://www.careermate.site/login?status=f");
            }
//            handleException(request, response, filterChain, CommonErrorCode.NO_ACCESS_TOKEN);
            return;
        }

        if (jwtUtil.isExpired(authorization)) {
            handleException(request, response, filterChain, CommonErrorCode.EXPIRED_TOKEN);
            return;
        }

        final Authentication authentication = jwtUtil.getAuthentication(authorization);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private void handleException(HttpServletRequest request, HttpServletResponse response,
                                 FilterChain filterChain, ErrorCode errorCode) throws ServletException, IOException{
        SecurityContextHolder.clearContext();
        request.setAttribute("authException", errorCode);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return EXCLUDE_URL_LIST.stream()
                .anyMatch(path::startsWith);
    }
}
