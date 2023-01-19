package com.masil.domain.member.service;

import com.masil.domain.member.dto.request.MemberCreateRequest;
import com.masil.domain.member.repository.MemberRepository;
import com.masil.global.auth.entity.Authority;
import com.masil.global.auth.model.MemberAuthType;
import com.masil.global.auth.repository.AuthorityRepository;
import com.masil.global.error.exception.BusinessException;
import com.masil.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public void getMyUser() {
    }

    public void modifyUser() {
    }

    public void modifyUserToDeleteState() {

    }
}
