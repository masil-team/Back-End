package com.masil.global.auth.service;

import com.masil.domain.member.dto.request.MemberAddressRequest;
import com.masil.domain.member.repository.MemberRepository;
import com.masil.domain.member.service.MemberService;
import com.masil.global.auth.dto.request.LoginRequest;
import com.masil.global.auth.dto.request.SignupRequest;
import com.masil.global.auth.dto.response.AuthMemberAdaptor;
import com.masil.global.auth.dto.response.AuthTokenResponse;
import com.masil.global.auth.dto.response.CurrentMember;
import com.masil.global.auth.dto.response.LoginMemberInfoResponse;
import com.masil.global.auth.entity.Authority;
import com.masil.global.auth.entity.RefreshToken;
import com.masil.global.auth.model.MemberAuthType;
import com.masil.global.auth.repository.AuthorityRepository;
import com.masil.global.auth.repository.RefreshTokenRepository;
import com.masil.global.error.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

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
    @DisplayName("????????? ??????")
    void loginSuccess () throws Exception {
        //given
        String testEmail = "test@gmail.com";
        String testPassword = "test@1234";
        String testPasswordConfirm = "test@1234";
        String testNickName = "??????????????????";

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
    @DisplayName("????????? ???????????? ?????????")
    void loginFailBecauseDuplicateEmail () throws Exception {
        //given

        String testEmail = "test@gmail.com";
        String testPassword = "test@1234";
        String testPasswordConfirm = "test@1234";
        String testNickName = "??????????????????";

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
                .nickname("?????????")
                .build())).isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("??????????????? ???????????? , ???????????? ????????? ?????? ??????")
    void signupFailBecauseNotEqualPasswordAndPasswordConfirm () throws Exception {
        //given
        String testEmail = "test@gmail.com";
        String testPassword = "test@1234";
        String testPasswordConfirm = "test@12345";
        String testNickName = "??????????????????";

        //when
        Assertions.assertThatThrownBy(() -> authService.signUp(SignupRequest.builder()
                .email(testEmail)
                .password(testPassword)
                .passwordConfirm(testPasswordConfirm)
                .nickname(testNickName)
                .build())).isInstanceOf(BusinessException.class);

    }

    @Test
    @DisplayName("?????? ?????? ????????? ??????")
    void reissueAccessTokenSuccess() throws Exception {
        //given

        //when

        //then
    }
    
    @Test
    @DisplayName("????????? ???????????? ???????????? ?????? ?????? ?????? ????????? ??????")
    void reissueFailBecauseInvalidRefreshTokenValue () throws Exception {
        //given
        
        //when
        
        //then
    }
    
    @Test
    @DisplayName("???????????? ?????? ????????? ?????? ?????? ?????? ????????? ??????")
    void reissueFailBecauseRefreshTokenIsExpired () throws Exception {
        //given
        
        //when
        
        //then
    }

    @Test
    @DisplayName("???????????? ???????????? ?????? ??????")
    void getLoginUserSuccess () throws Exception {

        //given
        SignupRequest testUser = SignupRequest.builder()
                .email("test1234567@naver.com")
                .nickname("????????? ?????????")
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
        LoginMemberInfoResponse memberInfo = authService.getLoginMemberInfo(afterAdaptor.getMember());
        //then
        assertEquals("????????? ?????????",memberInfo.getNickname());
        assertEquals("test1234567@naver.com",memberInfo.getEmail());
        assertEquals(11110103,memberInfo.getAddress().getEmdId());
    }

    @Test
    @DisplayName("???????????? - ??????")
    void userLogoutTestSuccess () throws Exception {
        //given
        String testEmail = "test@gmail.com";
        String testPassword = "test@1234";
        String testPasswordConfirm = "test@1234";
        String testNickName = "??????????????????";

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
        AuthTokenResponse token = authService.login(loginRequest);
        RefreshToken refreshToken = refreshTokenRepository.findByKey(testEmail).orElseThrow();
        AuthMemberAdaptor userDetails = (AuthMemberAdaptor) userDetailsService.loadUserByUsername(testEmail);

        //then
        assertEquals(token.getRefreshToken(), refreshToken.getValue());
        authService.logout(userDetails.getMember().getId(),userDetails.getMember());
        refreshToken = refreshTokenRepository.findByKey(testEmail).orElse(null);
        assertEquals(null, refreshToken);
    }
}