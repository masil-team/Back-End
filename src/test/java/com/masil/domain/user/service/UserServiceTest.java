package com.masil.domain.user.service;

import com.masil.domain.user.dto.UserCreateRequest;
import com.masil.domain.user.dto.UserLoginRequest;
import com.masil.domain.user.repository.UserRepository;
import com.masil.global.auth.jwt.dto.TokenInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void saveUserWhenSuccess () throws Exception {

        //given
        UserCreateRequest request = UserCreateRequest.builder()
                .email("test@naver.com")
                .nickname("테스트")
                .password("test123")
                .build();
        //when

        userService.signUp(request);

        //then
        assertEquals(1L,userRepository.findAll().size());
        assertEquals(request.getEmail(),userRepository.findAll().get(0).getEmail());
        assertEquals(request.getNickname(),userRepository.findAll().get(0).getNickname());
        assertEquals(request.getPassword(),userRepository.findAll().get(0).getPassword());

    }

    @Test
    @DisplayName("로그인 성공")
    void userLoginWhenSuccess () throws Exception {
        //given

        List<String> rolesRequest = new ArrayList<>();
        rolesRequest.add("USER");

        UserCreateRequest create = UserCreateRequest.builder()
                .email("test@naver.com")
                .password("test123")
                .nickname("테스트")
                .roles(rolesRequest)
                .build();

        userService.signUp(create);

        UserLoginRequest loginRequest = UserLoginRequest.builder()
                .email("test@naver.com")
                .password("test123")
                .build();

        //when
        TokenInfo token = userService.login(loginRequest);

        //then
        System.out.println("Token GrantType :" + token.getGrantType());
        System.out.println("Token AccessToken :" + token.getAccessToken());
        System.out.println("Token RefreshToken :" + token.getRefreshToken());
        assertNotNull(token);
    }


}