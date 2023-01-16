package com.masil.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class UserCreateRequest {

    private String email;
    private String password;
    private String nickname;
    private String profileImage;

    private List<String> roles;

    @Builder
    public UserCreateRequest(String email, String password, String nickname, String profileImage, List<String> roles) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.roles = roles;
    }
}
