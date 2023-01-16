package com.masil.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masil.domain.user.dto.UserCreateRequest;
import com.masil.domain.user.dto.UserLoginRequest;
import com.masil.domain.user.repository.UserRepository;
import com.masil.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @Test
    @DisplayName("회원 가입 성공시")
    void saveUserWhenIsOk () throws Exception {
        //given
        UserCreateRequest request = UserCreateRequest.builder()
                .email("test@naver.com")
                .password("test123")
                .nickname("테스트")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("잘못된 요청으로 회원 가입 실패시 ")
    void saveUserWhenBadRequest () throws Exception {
        //given
        UserCreateRequest request = UserCreateRequest.builder()
                .email("test@naver.com")
                .password("test123")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 성공시")
    void loginUserWhenIsOk () throws Exception {
        //given
        UserLoginRequest request = UserLoginRequest.builder()
                .email("test@naver.com")
                .password("test123")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("잘못된 요청으로 로그인 실패시")
    void loginUserWhenBadRequest () throws Exception {
        //given
        UserLoginRequest request = UserLoginRequest.builder()
                .email("test@naver.com")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }
}