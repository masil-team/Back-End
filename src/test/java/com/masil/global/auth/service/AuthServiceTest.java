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
        String testNickName = "테스트닉네임";

        SignupRequest createRequest = SignupRequest.builder()
                .email(testEmail)
                .password(testPassword)
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
    @DisplayName("이메일이나 비밀번호가 유효하지 않을 경우 실패")
    void loginFailBecauseBadRequest () throws Exception {
        //given
        
        //when
        
        //then
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