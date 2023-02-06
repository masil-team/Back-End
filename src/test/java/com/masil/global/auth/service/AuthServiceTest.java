package com.masil.global.auth.service;

import com.masil.common.annotation.ServiceTest;
import com.masil.domain.member.repository.MemberRepository;
import com.masil.domain.member.service.MemberService;
import com.masil.global.auth.dto.request.LoginRequest;
import com.masil.global.auth.dto.request.SignupRequest;
import com.masil.global.auth.dto.response.AuthTokenResponse;
import com.masil.global.auth.entity.Authority;
import com.masil.global.auth.model.MemberAuthType;
import com.masil.global.auth.repository.AuthorityRepository;
import com.masil.global.error.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest extends ServiceTest{

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @BeforeEach
    void saveAuthority() {
        Authority authority = Authority.builder()
                .authType(MemberAuthType.ROLE_USER)
                .build();

        authorityRepository.save(authority);
    }

    @AfterEach
    void deleteAuthority() {
        authorityRepository.deleteAll();
    }
    @Test
    @DisplayName("로그인 성공")
    void loginSuccess () throws Exception {
        //given
        String testEmail = "test@gmail.com";
        String testPassword = "test@1234";
        String testPasswordConfirm = "test@1234";
        String testNickName = "테스트닉네임";

        SignupRequest createRequest = SignupRequest.builder()
                .email(testEmail)
                .password(testPassword)
                .passwordConfirm(testPasswordConfirm)
                .nickname(testNickName)
                .build();

        authService.signUp(createRequest);

        LoginRequest loginRequest = LoginRequest.builder()
                .email(testEmail)
                .password(testPassword)
                .build();

        //when
        AuthTokenResponse tokenResponse = authService.login(loginRequest);

        //then
        System.out.println("AccessToken = " + tokenResponse.getAccessToken() );
        System.out.println("RefreshToken = " + tokenResponse.getRefreshToken() );
        assertNotNull(tokenResponse);
    }
    
    @Test
    @DisplayName("이메일 중복오류 테스트")
    void loginFailBecauseDuplicateEmail () throws Exception {
        //given

        String testEmail = "test@gmail.com";
        String testPassword = "test@1234";
        String testPasswordConfirm = "test@1234";
        String testNickName = "테스트닉네임";

        SignupRequest createRequest = SignupRequest.builder()
                .email(testEmail)
                .password(testPassword)
                .passwordConfirm(testPasswordConfirm)
                .nickname(testNickName)
                .build();

        authService.signUp(createRequest);

        //then
        Assertions.assertThatThrownBy(() -> authService.signUp(SignupRequest.builder()
                .email(testEmail)
                .password("234wetwer23")
                .passwordConfirm("234wetwer23")
                .nickname("닉네임")
                .build())).isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("회원가입시 비밀번호 , 비밀번호 확인이 다를 경우")
    void signupFailBecauseNotEqualPasswordAndPasswordConfirm () throws Exception {
        //given
        String testEmail = "test@gmail.com";
        String testPassword = "test@1234";
        String testPasswordConfirm = "test@12345";
        String testNickName = "테스트닉네임";

        //when
        Assertions.assertThatThrownBy(() -> authService.signUp(SignupRequest.builder()
                .email(testEmail)
                .password(testPassword)
                .passwordConfirm(testPasswordConfirm)
                .nickname(testNickName)
                .build())).isInstanceOf(BusinessException.class);

    }

    @Test
    @DisplayName("인증 토큰 재발행 성공")
    void reissueAccessTokenSuccess() throws Exception {
        //given

        //when

        //then
    }
    
    @Test
    @DisplayName("잘못된 리프레쉬 토큰으로 인한 인증 토큰 재발행 실패")
    void reissueFailBecauseInvalidRefreshTokenValue () throws Exception {
        //given
        
        //when
        
        //then
    }
    
    @Test
    @DisplayName("리프레쉬 토큰 만료로 인한 인증 토큰 재발행 실패")
    void reissueFailBecauseRefreshTokenIsExpired () throws Exception {
        //given
        
        //when
        
        //then
    }

}