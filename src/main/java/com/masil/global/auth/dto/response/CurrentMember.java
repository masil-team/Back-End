package com.masil.global.auth.dto.response;

import com.masil.domain.address.entity.EmdAddress;
import com.masil.domain.member.entity.Member;
import com.masil.global.auth.entity.Authority;
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class CurrentMember {

    private final Long id;
    private final String email;
    private final String password;
    private final String nickname;
    private final String profileImage;
    private final Set<Authority> authorities;
    private final EmdAddress address;

    @Builder
    public CurrentMember(Long id, String email, String password, String nickname, String profileImage, Set<Authority> authorities, EmdAddress address) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.authorities = authorities;
        this.address = address;
    }

    public static CurrentMember of(Member member) {

        if (member.getEmdAddress() == null) {
            return CurrentMember.builder()
                    .id(member.getId())
                    .email(member.getEmail())
                    .password(member.getPassword())
                    .nickname(member.getNickname())
                    .authorities(member.getAuthorities())
                    .build();
        }

        return CurrentMember.builder()
                .id(member.getId())
                .email(member.getEmail())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .authorities(member.getAuthorities())
                .address(member.getEmdAddress())
                .build();
    }
}