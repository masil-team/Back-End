package com.masil.domain.member.service;

import com.masil.domain.member.dto.request.MemberAddressRequest;
import com.masil.domain.member.entity.Member;
import com.masil.domain.member.repository.MemberRepository;
import com.masil.global.auth.dto.request.SignupRequest;
import com.masil.global.auth.service.AuthService;
import com.masil.global.error.exception.EntityNotFoundException;
import com.masil.global.error.exception.ErrorCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("주소 등록 및 수정 성공")
    void memberAddressModifySuccess () throws Exception {
        //given
        SignupRequest signReq = SignupRequest.builder()
                .email("테스트아이디")
                .password("1234")
                .passwordConfirm("1234")
                .nickname("닉네임1234")
                .build();
        authService.signUp(signReq);
        Member savedMember = memberRepository.findByEmail(signReq.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        Integer emdId = 11110108;

        MemberAddressRequest addressRequest = MemberAddressRequest.builder()
                .emdId(emdId)
                .build();

        //when
        memberService.modifyMemberAddress(savedMember.getId(), addressRequest);

        //then
        Member member = memberRepository.findByEmail(signReq.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        Assertions.assertEquals(emdId,member.getEmdAddress().getId());
    }
}