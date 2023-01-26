package com.masil.domain.comment.dto;

import com.masil.domain.post.dto.PostModifyRequest;

public class CommentModifyRequestBuilder {

    public static CommentModifyRequest build(String content) {
        return new CommentModifyRequest(content);
    }

    public static CommentModifyRequest build() {
        return new CommentModifyRequest("수정할 댓글");
    }
}
