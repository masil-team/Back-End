package com.masil.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masil.domain.member.dto.request.MemberCreateRequest;
import com.masil.domain.member.repository.MemberRepository;
import com.masil.domain.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

//@AutoConfigureMockMvc
//@SpringBootTest
@WebMvcTest
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;


    @Test
    @DisplayName("회원 가입 성공")
    void saveUserWhenIsOk () throws Exception {
        //given
        MemberCreateRequest request = MemberCreateRequest.builder()
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
    @DisplayName("이메일 중복으로 인한 회원 가입 실패")
    void saveUserBecauseDuplicateMail () throws Exception {
        //given
        MemberCreateRequest request = MemberCreateRequest.builder()
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
    @DisplayName("잘못된 비밀번호 패턴으로 인한 회원가입 실패")
    void saveUserFailBecauseWrongPasswordPattern () throws Exception {
        //given
        MemberCreateRequest request = MemberCreateRequest.builder()
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
}