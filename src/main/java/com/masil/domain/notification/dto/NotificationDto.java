package com.masil.domain.notification.dto;

import com.masil.domain.notification.entity.NotificationType;
import com.masil.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationDto {

    private NotificationType notificationType;
    private String content;
    private String url;


    public static NotificationDto of (NotificationType notificationType, Post post) {

        String content = notificationType.getMessage()
                .replace("*", post.getNotificationPreview());

        String url = "/post/" + post.getId();

        return NotificationDto.builder()
                .notificationType(notificationType)
                .content(content)
                .url(url)
                .build();
    }
}
