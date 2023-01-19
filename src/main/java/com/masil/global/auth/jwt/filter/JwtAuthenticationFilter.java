package com.masil.global.auth.jwt.filter;

import com.masil.global.auth.jwt.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer";
    private final JwtTokenProvider jwtTokenProvider;

//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        // 1. Request Header 에서 JWT 토큰 추출
//        String token = resolveToken((HttpServletRequest) request);
//
//        // 2. validateToken 으로 토큰 유효성 검사
//        if (token != null && jwtTokenProvider.validateToken(token)) {
//            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
//            Authentication authentication = jwtTokenProvider.getAuthentication(token);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//        chain.doFilter(request, response);
//    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);
        log.debug("token = {}", token);

        if (StringUtils.hasText(token)) {
            int flag = jwtTokenProvider.validateToken(token);
            log.debug("flag = {}", flag);
            // 토큰 유효함
            if (flag == 1) {
                this.setAuthentication(token);
            } else if (flag == 2) { // 토큰 만료
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                log.debug("doFilterInternal Exception CALL!");
                out.println("{\"error\": \"ACCESS_TOKEN_EXPIRED\", \"message\" : \"엑세스토큰이 만료되었습니다.\"}");
            } else { //잘못된 토큰
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                log.debug("doFilterInternal Exception CALL!");
                out.println("{\"error\": \"BAD_TOKEN\", \"message\" : \"잘못된 토큰 값입니다.\"}");
            }
        } else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.println("{\"error\": \"EMPTY_TOKEN\", \"message\" : \"토큰 값이 비어있습니다.\"}");
        }
    }

    /**
     * @param token 토큰이 유효한 경우 SecurityContext에 저장
     */
    private void setAuthentication(String token) {
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // Request Header 에서 토큰 정보를 꺼내오기
    private String resolveToken(HttpServletRequest request) {
        // bearer : 123123123123123 -> return 123123123123123123
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
