package com.masil.common.security;

import com.masil.global.auth.dto.response.AuthMemberAdaptor;
import com.masil.global.auth.dto.response.CurrentMember;
import com.masil.global.auth.entity.Authority;
import com.masil.global.auth.model.MemberAuthType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.getContext();

        Set<Authority> authoritySet = new HashSet<>();
        authoritySet.add(new Authority(MemberAuthType.ROLE_USER));

        List<SimpleGrantedAuthority> authList = authoritySet.stream()
                .map(Authority::getAuthorityNameToString)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        CurrentMember currentMember = CurrentMember.builder()
                .id(1L)
                .email("test123")
                .nickname("nickname")
                .password("12345")
                .authorities(authoritySet)
                .build();
        AuthMemberAdaptor authMemberAdaptor = new AuthMemberAdaptor(currentMember, authList);

        Authentication auth =new UsernamePasswordAuthenticationToken(authMemberAdaptor, "", authMemberAdaptor.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
