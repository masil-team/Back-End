package com.masil.global.auth.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class LoginRequest {
    @NotBlank(message = "잘못된 이메일입니다.")
    private final String email;
    @NotBlank(message = "잘못된 비밀번호입니다.")
    private final String password;

    @Builder
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
