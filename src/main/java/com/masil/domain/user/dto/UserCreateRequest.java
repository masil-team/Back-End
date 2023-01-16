package com.masil.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
public class UserCreateRequest {

    @NotBlank(message = "잘못된 이메일 값입니다.")
    private String email;
    @NotBlank(message = "잘못된 패스워드 값입니다.")
    private String password;
    @NotBlank(message = "잘못된 닉네임 값입니다.")
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
