package com.masil.global.auth.dto.response;

import com.masil.domain.member.entity.Member;
import com.masil.global.auth.entity.Authority;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class AuthMemberAdaptor extends User {

    private CurrentMember member;

    public AuthMemberAdaptor(Member member) {
        super(member.getEmail(), member.getPassword(), authorities(member.getAuthorities()));
        this.member = CurrentMember.of(member);
    }

    private static Collection<? extends GrantedAuthority> authorities(Set<Authority> roles) {
        return roles.stream()
                .map(Authority::getAuthorityNameToString)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
