package com.masil.domain.user.service;

import com.masil.domain.user.dto.UserCreateRequest;
import com.masil.domain.user.dto.UserLoginRequest;
import com.masil.domain.user.entity.User;
import com.masil.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    public void signUp(UserCreateRequest userRequest) {
        User user = User.builder()
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .nickname(userRequest.getNickname())
                .build();
        userRepository.save(user);

    }

    public void login(UserLoginRequest request) {

    }

    public void getMyUser() {
    }

    public void modifyUser() {
    }

    public void modifyUserToDeleteState() {

    }

    public void logout() {

    }
}
