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