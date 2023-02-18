package com.masil.domain.notification.service;

import com.masil.common.annotation.ServiceTest;
import com.masil.domain.fixture.MemberFixture;
import com.masil.domain.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class NotificationServiceTest extends ServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Test
    @DisplayName("클라이언트의 알림 연결을 성공한다.")
    void createConnection_success() {
        //given
        Member member = MemberFixture.일반_회원_JJ.엔티티_생성();
        String lastEventId = "";

        // when
        SseEmitter subscribe = notificationService.createConnection(member.getId(), lastEventId);

        // then
        assertThat(subscribe).isNotNull();
    }
}