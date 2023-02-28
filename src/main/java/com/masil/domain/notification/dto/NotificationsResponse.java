package com.masil.domain.notification.dto;

import com.masil.domain.notification.entity.Notification;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class NotificationsResponse {

    private List<NotificationResponse> notifications;

    public NotificationsResponse(List<NotificationResponse> notificationResponses) {
        this.notifications = notificationResponses;
    }

    public static NotificationsResponse ofNotifications(List<Notification> notifications) {
        List<NotificationResponse> notificationsResponse = notifications
                .stream()
                .map(notification -> NotificationResponse.of(notification))
                .collect(Collectors.toList());

        return new NotificationsResponse(notificationsResponse);
    }
}
