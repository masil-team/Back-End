package com.masil.domain.notification.dto;

import com.masil.domain.member.dto.response.MemberResponse;
import com.masil.domain.notification.entity.Notification;
import lombok.Builder;
import lombok.Getter;


import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponse {


    private Long id;

    private MemberResponse sender;

    private String content;

    private String url;

    private Boolean isRead;

    private LocalDateTime createDate;


    public static NotificationResponse of(Notification notification) {

        return NotificationResponse.builder()
                .id(notification.getId())
                .sender(MemberResponse.of(notification.getSender()))
                .content(notification.getContent())
                .url(notification.getUrl())
                .isRead(notification.getIsRead())
                .createDate(notification.getCreateDate())
                .build();
    }
}
