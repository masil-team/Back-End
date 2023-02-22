package com.masil.global.config.security;

import com.masil.global.auth.jwt.filter.JwtAuthenticationFilter;
import com.masil.global.auth.jwt.filter.JwtExceptionHandlerFilter;
import com.masil.global.auth.jwt.provider.JwtTokenProvider;
import com.masil.global.config.properties.AccessRequestMatcherAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtTokenProvider tokenProvider;
    private final AccessRequestMatcherAdapter requestMatcherAdaptor;
    @Override
    public void configure(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(tokenProvider, requestMatcherAdaptor);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JwtExceptionHandlerFilter(), JwtAuthenticationFilter.class);
    }
}
