package com.masil.global.auth.controller;

import com.masil.global.auth.annotaion.LoginUser;
import com.masil.global.auth.dto.request.AuthTokenRequest;
import com.masil.global.auth.dto.request.LoginRequest;
import com.masil.global.auth.dto.request.SignupRequest;
import com.masil.global.auth.dto.response.AuthTokenResponse;
import com.masil.global.auth.dto.response.CurrentMember;
import com.masil.global.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public void signUp(@Valid @RequestBody SignupRequest signupRequest) {
        authService.signUp(signupRequest);
    }
    @PostMapping("/auth/login")
    public AuthTokenResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }
    @PostMapping("/members/{memberId}/logout")
    public void logout(@PathVariable("memberId") Long memberId,@LoginUser CurrentMember member) {
        authService.logout(memberId,member);
    }

    @PostMapping("/auth/reissue")
    public AuthTokenResponse reissue(@RequestBody AuthTokenRequest tokenRequest) {
        return authService.reissue(tokenRequest);
    }

}
