package com.masil.domain.user.service;

import com.masil.domain.user.dto.UserCreateRequest;
import com.masil.domain.user.dto.UserLoginRequest;
import com.masil.domain.user.entity.User;
import com.masil.domain.user.repository.UserRepository;
import com.masil.global.auth.jwt.component.JwtTokenProvider;
import com.masil.global.auth.jwt.dto.TokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signUp(UserCreateRequest userRequest) {
        User user = User.builder()
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .nickname(userRequest.getNickname())
                .build();
        userRepository.save(user);

    }
    @Transactional
    public TokenInfo login(UserLoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        return tokenInfo;
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
