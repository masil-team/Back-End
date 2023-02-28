package com.masil.domain.notification.controller;

import com.masil.domain.notification.dto.NotificationsResponse;
import com.masil.domain.notification.service.NotificationService;
import com.masil.global.auth.annotaion.LoginUser;
import com.masil.global.auth.dto.response.CurrentMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * sse 연결 요청
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/sse", produces = "text/event-stream")
    public SseEmitter createConnection(@LoginUser CurrentMember currentMember) {
        log.info("sse 연결 시작");
        return notificationService.createConnection(currentMember.getId());
    }


    /**
     * 알람 조회 요청
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/notifications")
    public ResponseEntity<NotificationsResponse> findNotifications(@LoginUser CurrentMember currentMember) {
        return ResponseEntity.ok().body(notificationService.findNotifications(currentMember.getId()));
    }

    /**
     * 알람 읽음 요청
     */
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/notifications/{id}")
    public ResponseEntity<Map<String, Boolean>> readNotification(@PathVariable Long id,
                                                                 @LoginUser CurrentMember currentMember) {

        boolean isDisplay = notificationService.readNotification(id, currentMember.getId());
        return ResponseEntity.ok(Map.of("isDisplay", isDisplay));
    }

}
