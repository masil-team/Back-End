package com.masil.domain.notification.entity;

import com.masil.domain.member.entity.Member;
import com.masil.domain.post.entity.Post;

public enum NotificationType {
    POST_COMMENT_REPLY("님이 *에 댓글을 달았습니다."),
    POST_LIKE("님이 *에 좋아요를 하였습니다."),
    COMMENT_REPLY("님이 *에 남긴 댓글에 대댓글을 달았습니다."),
    COMMENT_LIKE("님이 *에 남긴 댓글에 좋아요를 하였습니다.");

    private String message;

    NotificationType(String message) {
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }
}
