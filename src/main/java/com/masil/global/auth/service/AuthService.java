package com.masil.global.auth.service;

import com.masil.domain.member.entity.Member;
import com.masil.domain.member.repository.MemberRepository;
import com.masil.global.auth.dto.request.AuthTokenRequest;
import com.masil.global.auth.dto.request.LoginRequest;
import com.masil.global.auth.dto.request.SignupRequest;
import com.masil.global.auth.dto.response.AuthTokenResponse;
import com.masil.global.auth.dto.response.CurrentMember;
import com.masil.global.auth.dto.response.LoginMemberInfoResponse;
import com.masil.global.auth.entity.Authority;
import com.masil.global.auth.entity.RefreshToken;
import com.masil.global.auth.jwt.provider.JwtTokenProvider;
import com.masil.global.auth.model.MemberAuthType;
import com.masil.global.auth.repository.AuthorityRepository;
import com.masil.global.auth.repository.RefreshTokenRepository;
import com.masil.global.error.exception.BusinessException;
import com.masil.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignupRequest signupRequest) {
        Set<Authority> authorites = new HashSet<>();

        authorites.add(authorityRepository.findByAuthorityName(MemberAuthType.ROLE_USER)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE)));

        validateSignRequest(signupRequest);
        memberRepository.save(signupRequest.convertMember(passwordEncoder, authorites));
    }

    private void validateSignRequest(SignupRequest createRequest) {
        if (checkDuplicateEmail(createRequest)) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }

        if (!createRequest.getPassword().equals(createRequest.getPasswordConfirm())) {
            throw new BusinessException(ErrorCode.NOT_SAME_PASSWORD_CONFIRM);
        }

        if (checkDuplicateNickName(createRequest)) {
            throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
        }
    }

    private boolean checkDuplicateNickName(SignupRequest createRequest) {
        return memberRepository.findByNickname(createRequest.getNickname()).isPresent();
    }

    private boolean checkDuplicateEmail(SignupRequest createRequest) {
        return memberRepository.findByEmail(createRequest.getEmail()).isPresent();
    }

    @Transactional
    public AuthTokenResponse login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        // TODO: 2023/02/06 메서드 크기가 커서 분리 필요  , 비밀번호 틀렸을 경우 Exception 처리
        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        String email = authenticate.getName();
        Member member = getMember(email);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(email, member.getAuthorities());
        String refreshToken = jwtTokenProvider.createRefreshToken(email, member.getAuthorities());

        // 리프레쉬 토큰 DB 저장
        RefreshToken savedToken;

        if (isSavedRefreshToken(email)) {
            savedToken = refreshTokenRepository.findByKey(email)
                    .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE));
            savedToken.updateValue(refreshToken);
        } else {
            savedToken = refreshTokenRepository.save(RefreshToken.builder()
                    .key(email)
                    .value(refreshToken)
                    .build());
        }

        log.debug("refreshToken = key : {} , value : {} ", savedToken.getKey(), savedToken.getValue());

        return jwtTokenProvider.createTokenResponse(accessToken, refreshToken);
    }

    private boolean isSavedRefreshToken(String email) {
        return refreshTokenRepository.findByKey(email).isPresent();
    }

    @Transactional
    public AuthTokenResponse reissue(AuthTokenRequest tokenRequest) {
        String orgAccessToken = tokenRequest.getAccessToken();
        String orgRefreshToken = tokenRequest.getRefreshToken();

        // TODO: 2023/02/06 메서드 크기가 큼 리팩토링 필요 
        // refreshToken 검증
        if (jwtTokenProvider.validateRefreshToken(orgRefreshToken)) {
            // 2. Access Token 에서 Member Email 가져오기
            Authentication authentication = jwtTokenProvider.getAuthentication(orgAccessToken);

            log.debug("Authentication = {}", authentication);

            // 3. 저장소에서 Member Email 를 기반으로 Refresh Token 값 가져옴
            RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                    .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN)); // 로그 아웃된 사용자


            // 4. Refresh Token 일치하는지 검사
            if (!refreshToken.getValue().equals(orgRefreshToken)) {
                throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN); // 토큰이 일치하지 않습니다.
            }

            // 5. 새로운 토큰 생성
            String email = jwtTokenProvider.getMemberEmailByToken(orgAccessToken);
            Member member = getMember(email);

            String newAccessToken = jwtTokenProvider.createAccessToken(email, member.getAuthorities());
            String newRefreshToken = jwtTokenProvider.createRefreshToken(email, member.getAuthorities());
            AuthTokenResponse tokenResponse = jwtTokenProvider.createTokenResponse(newAccessToken, newRefreshToken);

            log.debug("refresh Origin = {}", orgRefreshToken);
            log.debug("refresh New = {} ", newRefreshToken);

            // 6. 저장소 정보 업데이트 (dirtyChecking으로 업데이트)
//            refreshToken.updateValue(newRefreshToken);
            return tokenResponse;
        } else {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    private Member getMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE));
    }

    @Transactional
    public void logout(Long memberId,CurrentMember member) {

        if (validateLogInUser(memberId, member)) {
            // 로그 아웃 시 DB에 있는 리프레쉬  토큰 삭제
            RefreshToken refreshToken = refreshTokenRepository.findByKey(member.getEmail())
                    .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE));
            refreshTokenRepository.delete(refreshToken);
        } else {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }

    private boolean validateLogInUser(Long memberId, CurrentMember member) {
        return memberId == member.getId();
    }

    @Transactional
    public LoginMemberInfoResponse getMemberInfo(CurrentMember member) {
        if (member == null) {
            throw new BusinessException(ErrorCode.UNAUTHENTICATED_LOGIN_USER);
        }
        return LoginMemberInfoResponse.of(member);
    }
}
