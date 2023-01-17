package com.masil.global.auth.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {

    @Test
    @DisplayName("로그인 성공")
    void loginSuccess () throws Exception {

        //given

        //expected

    }

    @Test
    @DisplayName("잘못된 이메일로 인해 로그인 실패")
    void loginFailBecauseWrongEmail () throws Exception {

        //given

        //expected

    }

    @Test
    @DisplayName("잘못된 비밀번호로 인해 로그인 실패")
    void loginFailBecauseWrongPassword () throws Exception {

        //given

        //expected

    }

    @Test
    @DisplayName("액세스 토큰 만료로 인해 재발행")
    void reissueTestWhenAcceccTokenExpired () throws Exception {

        //given

        //expected

    }

    @Test
    @DisplayName("리프레쉬 토큰이 없을 경우 재 로그인 요청")
    void reloginWhenEmptyRefreshToken () throws Exception {

        //given

        //expected

    }

}