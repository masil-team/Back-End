package com.masil.domain.comment.dto;

public class CommentCreate411RequestBuilder {

    public static CommentCreateRequest build(String content) {
        return new CommentCreateRequest(content);
    }

    public static CommentCreateRequest build() {
        return new CommentCreateRequest("댓글 내용이 250자를 넘었습니다. 댓글 내용이 250자를 넘었습니다. 댓글 내용이 250자를 넘었습니다. 댓글 내용이 250자를 넘었습니다. 댓글 내용이 250자를 넘었습니다. 댓글은 250");
    }
}
