package com.masil.domain.notification.controller;

import com.masil.domain.notification.service.NotificationService;
import com.masil.global.auth.annotaion.LoginUser;
import com.masil.global.auth.dto.response.CurrentMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/sse", produces = "text/event-stream")
    public SseEmitter createConnection(@LoginUser CurrentMember currentMember,
                                       @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        log.info("sse 연결 시작, lastEventId={}", lastEventId);
        return notificationService.createConnection(currentMember.getId(), lastEventId);
    }

}
