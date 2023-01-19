package com.masil.domain.member.dto.request;

import com.masil.domain.member.entity.Member;
import com.masil.global.auth.entity.Authority;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@Getter
public class MemberCreateRequest {

    @NotBlank(message = "잘못된 이메일 값입니다.")
    private String email;
    @NotBlank(message = "잘못된 패스워드 값입니다.")
    private String password;

    @NotBlank(message = "잘못된 비밀번호 확인 입니다.")
    private String passwordConfirm;
    @NotBlank(message = "잘못된 닉네임 값입니다.")
    private String nickname;
    private String profileImage;

    @Builder
    public MemberCreateRequest(String email, String password, String passwordConfirm, String nickname, String profileImage, List<String> roles) {
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public Member convertMember(PasswordEncoder encoder , Set<Authority> authorities) {
        return Member.builder()
                .email(email)
                .password(encoder.encode(password))
                .nickname(nickname)
                .authorities(authorities)
                .build();
    }

}
