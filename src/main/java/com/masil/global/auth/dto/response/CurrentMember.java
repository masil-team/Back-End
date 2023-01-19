package com.masil.global.auth.dto.response;

import com.masil.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CurrentMember {

    private final Long id;
    private final String email;
    private final String password;
    private final String nickname;
    private final String profileImage;

    @Builder
    public CurrentMember(Long id, String email, String password, String nickname, String profileImage) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public static CurrentMember of(Member member) {
        return CurrentMember.builder()
                .id(member.getId())
                .email(member.getEmail())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .build();
    }
}
