package com.masil.domain.notification.controller;

import com.masil.domain.notification.service.NotificationService;
import com.masil.global.auth.annotaion.LoginUser;
import com.masil.global.auth.dto.response.CurrentMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/sse", produces = "text/event-stream")
    public SseEmitter createConnection(@LoginUser CurrentMember currentMember,
                                @RequestParam(value = "lastEventId", required = false, defaultValue = "") String lastEventId) {
        return notificationService.createConnection(currentMember.getId(), lastEventId);
    }

}
