package com.masil.domain.member.service;

import com.masil.domain.member.dto.request.MemberCreateRequest;
import com.masil.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입 성공")
    void saveUserWhenSuccess () throws Exception {

        //given
        MemberCreateRequest request = MemberCreateRequest.builder()
                .email("test@naver.com")
                .nickname("테스트")
                .password("test123")
                .build();
        //when
        memberService.signUp(request);

        //then
        assertEquals(1L, memberRepository.findAll().size());
        assertEquals(request.getEmail(), memberRepository.findAll().get(0).getEmail());
        assertEquals(request.getNickname(), memberRepository.findAll().get(0).getNickname());
        assertEquals(request.getPassword(), memberRepository.findAll().get(0).getPassword());
    }

    @Test
    @DisplayName("이메일 중복으로 인한 회원가입 실패")
    void saveUserFailBecauseDuplicateEmail () throws Exception {
        //given

        //when

        //then
    }

    @Test
    @DisplayName("비밀번호 양식 잘못 입력으로 인한 회원가입 실패")
    void saveUserFailBecauseWrongPasswordPattern () throws Exception {
        //given

        //when

        //then
    }

}