package com.masil.global.auth.jwt.provider;

import com.masil.global.auth.dto.response.AuthTokenResponse;
import com.masil.global.auth.entity.Authority;
import com.masil.global.auth.jwt.properties.JwtProperties;
import com.masil.global.auth.service.CustomUserDetailsService;
import com.masil.global.error.exception.BusinessException;
import com.masil.global.error.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";

    private final Key key;
    private final long ACCESS_TOKEN_EXPIRE_TIME;
    private final long REFRESH_TOKEN_EXPIRE_TIME;
    private final CustomUserDetailsService userDetailsService;
//    private final CustomUserDetailsService userDetailsService;

    public JwtTokenProvider(JwtProperties jwtProperties, CustomUserDetailsService userDetailsService) {
        byte[] keyBytes = jwtProperties.getSecret().getBytes();
        key = Keys.hmacShaKeyFor(keyBytes);
        ACCESS_TOKEN_EXPIRE_TIME = jwtProperties.getAccessTokenExpireTime();
        REFRESH_TOKEN_EXPIRE_TIME = jwtProperties.getRefreshTokenExpireTime();
        this.userDetailsService = userDetailsService;
    }

    protected String createToken(String email, Set<Authority> auth, long tokenValid) {
        // ex) sub : abc@abc.com
        Claims claims = Jwts.claims().setSubject(email);

        // ex)  auth : ROLE_USER,ROLE_ADMIN
        claims.put(AUTHORITIES_KEY,
                auth.stream()
                        .map(Authority::getAuthorityNameToString)
                        .collect(Collectors.joining(","))
        );

        // 현재시간
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims) // 토큰 발행 유저 정보
                .setIssuedAt(now) // 토큰 발행 시간
                .setExpiration(new Date(now.getTime() + tokenValid)) // 토큰 만료시간
                .signWith(key,SignatureAlgorithm.HS512) // 키와 알고리즘 설정
                .compact();
    }

    public AuthTokenResponse createTokenResponse(String accessToken, String refreshToken) {
        return AuthTokenResponse.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String createAccessToken(String email,Set<Authority> auth) {
        return this.createToken(email,auth,ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String createRefreshToken(String email,Set<Authority> auth) {
        return this.createToken(email,auth,REFRESH_TOKEN_EXPIRE_TIME);
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
//        UserDetails principal = new User(claims.getSubject(), "", authorities);
        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException ex){
            throw new BusinessException("Invalid JWT signature", ErrorCode.INVALID_INPUT_VALUE);
        } catch (MalformedJwtException ex) {
            throw new BusinessException("Invalid JWT token", ErrorCode.INVALID_INPUT_VALUE);
        } catch (ExpiredJwtException ex) {
            throw new BusinessException("Expired JWT token", ErrorCode.INVALID_INPUT_VALUE);
        } catch (UnsupportedJwtException ex) {
            throw new BusinessException("Unsupported JWT token", ErrorCode.INVALID_INPUT_VALUE);
        } catch (IllegalArgumentException ex) {
            throw new BusinessException("JWT claims string is empty.", ErrorCode.INVALID_INPUT_VALUE);
        }
    }


    /**
     *
     * @param token
     * @return 토큰 값을 파싱하여 클레임에 담긴 이메일 값을 가져온다.
     */
    public String getMemberEmailByToken(String token) {
        // 토큰의 claim 의 sub 키에 이메일 값이 들어있다.
        return this.parseClaims(token).getSubject();
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
