package com.masil.global.auth.jwt.config;

import com.masil.global.auth.jwt.properties.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {JwtProperties.class})
public class JwtPropertiesConfig {
}
