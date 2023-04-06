package com.masil.global.auth.controller;

import com.masil.global.auth.annotaion.LoginUser;
import com.masil.global.auth.dto.request.AuthTokenRequest;
import com.masil.global.auth.dto.request.LoginRequest;
import com.masil.global.auth.dto.request.SignupRequest;
import com.masil.global.auth.dto.response.AuthTokenResponse;
import com.masil.global.auth.dto.response.CurrentMember;
import com.masil.global.auth.dto.response.LoginMemberInfoResponse;
import com.masil.global.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/guest-available/auth/signup")
    public void signUp(@Valid @RequestBody SignupRequest signupRequest) {
        authService.signUp(signupRequest);
    }
    @PostMapping("/guest-available/auth/login")
    public AuthTokenResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }
    @PostMapping("/auth/logout")
    public void logout(@LoginUser CurrentMember member) {
        authService.logout(member);
    }

    @PostMapping("/auth/reissue")
    public AuthTokenResponse reissue(@RequestBody AuthTokenRequest tokenRequest) {
        return authService.reissueAccessToken(tokenRequest);
    }

    @GetMapping("/login-user")
    public LoginMemberInfoResponse getMemberInfo(@LoginUser CurrentMember member) {
        return authService.getLoginMemberInfo(member);
    }
}
