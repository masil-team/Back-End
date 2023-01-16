package com.masil.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserCreateRequest {

    private String email;
    private String password;
    private String nickname;
    private String profileImage;

    @Builder
    public UserCreateRequest(String email, String password, String nickname, String profileImage) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}
