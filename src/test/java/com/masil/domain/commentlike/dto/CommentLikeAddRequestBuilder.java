package com.masil.domain.commentlike.dto;

import com.masil.domain.comment.dto.ChildrenCreateRequest;

public class CommentLikeAddRequestBuilder {

    public static CommentLikeResponse build(int likeCount, boolean status) {
        return new CommentLikeResponse(likeCount, status);
    }

    public static CommentLikeResponse build() {
        return new CommentLikeResponse(1, true);
    }
}
