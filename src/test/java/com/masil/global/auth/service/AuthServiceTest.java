package com.masil.global.auth.service;

import com.masil.common.annotation.ServiceTest;
import com.masil.domain.member.dto.request.MemberAddressRequest;
import com.masil.domain.member.entity.Member;
import com.masil.domain.member.repository.MemberRepository;
import com.masil.domain.member.service.MemberService;
import com.masil.global.auth.dto.request.LoginRequest;
import com.masil.global.auth.dto.request.SignupRequest;
import com.masil.global.auth.dto.response.AuthMemberAdaptor;
import com.masil.global.auth.dto.response.AuthTokenResponse;
import com.masil.global.auth.dto.response.CurrentMember;
import com.masil.global.auth.dto.response.LoginMemberInfoResponse;
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
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserDetailsService userDetailsService;

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

    @Test
    @DisplayName("로그인한 유저정보 조회 성공")
    void getLoginUserSuccess () throws Exception {

        //given
        SignupRequest testUser = SignupRequest.builder()
                .email("test1234567@naver.com")
                .nickname("테스트 닉네임")
                .password("1234")
                .passwordConfirm("1234")
                .build();

        authService.signUp(testUser);

        UserDetails userDetails = userDetailsService.loadUserByUsername("test1234567@naver.com");
        AuthMemberAdaptor adaptor = (AuthMemberAdaptor) userDetails;

        CurrentMember member = adaptor.getMember();
        member.getId();
        MemberAddressRequest addressRequest = MemberAddressRequest.builder()
                .emdId(11110103)
                .build();

        memberService.modifyMemberAddress(member.getId(),addressRequest);

        AuthMemberAdaptor afterAdaptor = (AuthMemberAdaptor) userDetailsService.loadUserByUsername("test1234567@naver.com");

        //when
        LoginMemberInfoResponse memberInfo = authService.getMemberInfo(afterAdaptor.getMember());
        //then
        assertEquals("테스트 닉네임",memberInfo.getNickname());
        assertEquals("test1234567@naver.com",memberInfo.getEmail());
        assertEquals(11110103,memberInfo.getAddress().getEmdId());
    }

}