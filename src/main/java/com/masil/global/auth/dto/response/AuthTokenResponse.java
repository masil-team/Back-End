package com.masil.global.auth.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthTokenResponse {
    private String grantType;
    private String accessToken;
    private String refreshToken;

    @Builder
    public AuthTokenResponse(String grantType, String accessToken, String refreshToken) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
