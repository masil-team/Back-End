package com.masil.global.auth.jwt.filter;

import com.masil.global.auth.jwt.provider.JwtTokenProvider;
import com.masil.global.config.properties.AccessRequestMatcherAdaptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;
    private final AccessRequestMatcherAdaptor requestMatcherAdaptor;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        // TODO: 2023/02/06 추후 변경하기  
//        String header = request.getHeader(AUTHORIZATION_HEADER);
//        if (header == null || !header.startsWith(BEARER_PREFIX) && HttpMethod.GET.equals(request.getMethod())) {
//            filterChain.doFilter(request, response);
//            return;
//        }
        String token = resolveToken(request);
        log.debug("token = {}", token);

        if (StringUtils.hasText(token)) {
            jwtTokenProvider.validateTokenOnFilter(token);
            // 토큰 유효함
            this.setAuthentication(token);
        } else {

        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        if (requestMatcherAdaptor.getRequestMatcher().matches(request) &&
                request.getServletPath().startsWith("/auth") || request.getMethod().equals("GET")) {
            return true;
        }
        return false;
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
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
