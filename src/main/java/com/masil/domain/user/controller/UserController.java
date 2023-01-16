package com.masil.domain.user.controller;

import com.masil.domain.user.dto.UserCreateRequest;
import com.masil.domain.user.dto.UserLoginRequest;
import com.masil.domain.user.service.UserService;
import com.masil.global.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping
    public String hello() {
        return "Hello";
    }

    @PostMapping
    public void signUp(@Valid @RequestBody UserCreateRequest userRequest) {
        userService.signUp(userRequest);
    }

    @PostMapping("/login")
    public void login(@Valid @RequestBody UserLoginRequest request) {
        userService.login(request);

    }

    @GetMapping("/{userId}")
    public void getMyUser() {
        userService.getMyUser();

    }

    @PatchMapping("/{userId}")
    public void modifyUser() {
        userService.modifyUser();
    }

    @DeleteMapping("/{userId}")
    public void modifyUserToDeleteState() {
        userService.modifyUserToDeleteState();
    }

    @GetMapping("/logout")
    public void logout() {
        userService.logout();
    }

}
