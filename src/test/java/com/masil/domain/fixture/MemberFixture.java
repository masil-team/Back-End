package com.masil.domain.fixture;

import com.masil.domain.address.entity.EmdAddress;
import com.masil.domain.member.entity.Member;

public enum MemberFixture {
    일반_회원_JJ("jj123@gmail.com", "jj123123!", "지지"),
    일반_회원_KK("kk123@gmail.com", "kk123123!", "케이케이")
    ;

    private final String email;
    private final String password;
    private final String nickname;

    MemberFixture(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
    public Member 엔티티_생성() {
        return Member.builder()
                .email(this.email)
                .password(this.password)
                .nickname(this.nickname)
                .build();
    }
}
