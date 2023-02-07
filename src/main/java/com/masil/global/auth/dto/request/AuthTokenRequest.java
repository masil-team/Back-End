package com.masil.global.auth.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthTokenRequest {

    private String accessToken;
    private String refreshToken;

    @Builder
    public AuthTokenRequest(String grantType, String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
