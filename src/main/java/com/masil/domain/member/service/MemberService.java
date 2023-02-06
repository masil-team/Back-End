package com.masil.domain.member.service;

import com.masil.domain.address.entity.EmdAddress;
import com.masil.domain.address.repository.EmdAddressRepository;
import com.masil.domain.member.dto.request.MemberAddressRequest;
import com.masil.domain.member.entity.Member;
import com.masil.domain.member.repository.MemberRepository;
import com.masil.global.auth.repository.AuthorityRepository;
import com.masil.global.error.exception.EntityNotFoundException;
import com.masil.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final EmdAddressRepository emdAddressRepository;
    private final PasswordEncoder passwordEncoder;

    public void getMyUser() {
    }

    public void modifyUser() {
    }

    public void modifyUserToDeleteState() {

    }

    public void modifyMemberAddress(Long memberId, MemberAddressRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        EmdAddress emdAddress = emdAddressRepository.findById(request.getRCode())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        member.updateEmdAddress(emdAddress);
    }
}
