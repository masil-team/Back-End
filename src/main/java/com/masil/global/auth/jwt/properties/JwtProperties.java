package com.masil.global.auth.jwt.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("jwt")
public class JwtProperties {

    private final String header;
    private final String secret;
    private final long accessTokenExpireTime;
    private final long refreshTokenExpireTime;

}



