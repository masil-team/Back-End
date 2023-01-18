package com.masil.global.auth.service;

import com.masil.domain.member.entity.Member;
import com.masil.domain.member.repository.MemberRepository;
import com.masil.global.auth.context.CurrentMemberContext;
import com.masil.global.auth.dto.request.AuthTokenRequest;
import com.masil.global.auth.dto.request.LoginRequest;
import com.masil.global.auth.dto.response.AuthTokenResponse;
import com.masil.global.auth.entity.RefreshToken;
import com.masil.global.auth.jwt.provider.JwtTokenProvider;
import com.masil.global.auth.repository.RefreshTokenRepository;
import com.masil.global.auth.util.AuthUtil;
import com.masil.global.error.exception.BusinessException;
import com.masil.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthTokenResponse login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        String email = authenticate.getName();
        Member member = getMember(email);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(email, member.getAuthorities());
        String refreshToken = jwtTokenProvider.createRefreshToken(email, member.getAuthorities());

        // 리프레쉬 토큰 DB 저장
        RefreshToken save = refreshTokenRepository.save(
                RefreshToken.builder()
                        .key(email)
                        .value(refreshToken)
                        .build()
        );

        log.debug("refreshToken = key : {} , value : {} ", save.getKey(), save.getValue());

        return jwtTokenProvider.createTokenResponse(accessToken,refreshToken);
    }

    @Transactional
    public AuthTokenResponse reissue(AuthTokenRequest tokenRequest) {

        String orgAccessToken = tokenRequest.getAccessToken();
        String orgRefreshToken = tokenRequest.getRefreshToken();

        // refreshToken 검증
        int refreshTokenFlag = jwtTokenProvider.validateToken(orgRefreshToken);

        log.debug("refreshTokenFlag = {}", refreshTokenFlag);

        //refreshToken 검증하고 상황에 맞는 오류를 내보낸다.
        if (refreshTokenFlag == -1) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE); // 잘못된 리프레시 토큰
        } else if (refreshTokenFlag == 2) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE); // 유효기간 끝난 토큰
        }

        // 2. Access Token 에서 Member Email 가져오기
        Authentication authentication = jwtTokenProvider.getAuthentication(orgAccessToken);

        log.debug("Authentication = {}",authentication);

        // 3. 저장소에서 Member Email 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE)); // 로그 아웃된 사용자


        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(orgRefreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE); // 토큰이 일치하지 않습니다.
        }

        // 5. 새로운 토큰 생성
        String email = jwtTokenProvider.getMemberEmailByToken(orgAccessToken);
        Member member = getMember(email);

        String newAccessToken = jwtTokenProvider.createAccessToken(email, member.getAuthorities());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(email, member.getAuthorities());
        AuthTokenResponse tokenResponse = jwtTokenProvider.createTokenResponse(newAccessToken, newRefreshToken);

        log.debug("refresh Origin = {}",orgRefreshToken);
        log.debug("refresh New = {} ",newRefreshToken);

        // 6. 저장소 정보 업데이트 (dirtyChecking으로 업데이트)
        refreshToken.updateValue(newRefreshToken);

        return tokenResponse;
    }

    private Member getMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(()->new BusinessException(ErrorCode.INVALID_INPUT_VALUE));
    }

    public void logout() {
    }

    public CurrentMemberContext getCurrentMemberContext() {
        String currentMemberEmail = AuthUtil.getCurrentMemberEmail();

        Member member = memberRepository.findByEmail(currentMemberEmail)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE));

        return CurrentMemberContext.builder()
                .id(member.getId())
                .eamil(member.getEmail())
                .build();
    }
}
