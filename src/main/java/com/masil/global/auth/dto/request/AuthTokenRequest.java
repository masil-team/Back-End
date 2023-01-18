package com.masil.global.auth.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthTokenRequest {

    private String grantType;
    private String accessToken;
    private String refreshToken;

    @Builder
    public AuthTokenRequest(String grantType, String accessToken, String refreshToken) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
