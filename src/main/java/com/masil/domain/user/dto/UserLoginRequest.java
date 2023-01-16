package com.masil.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class UserLoginRequest {
    @NotBlank(message = "잘못된 이메일입니다.")
    private final String email;
    @NotBlank(message = "잘못된 비밀번호입니다.")
    private final String password;

    @Builder
    public UserLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
