package com.masil.global.auth.controller;

import com.masil.global.auth.dto.request.AuthTokenRequest;
import com.masil.global.auth.dto.request.LoginRequest;
import com.masil.global.auth.dto.response.AuthTokenResponse;
import com.masil.global.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthTokenResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/logout")
    public void logout() {
        authService.logout();
    }

    @PostMapping("/reissue")
    public AuthTokenResponse reissue(@RequestBody AuthTokenRequest tokenRequest) {
        return authService.reissue(tokenRequest);
    }

}
