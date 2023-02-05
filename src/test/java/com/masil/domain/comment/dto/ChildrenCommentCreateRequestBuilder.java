package com.masil.domain.comment.dto;

public class ChildrenCommentCreateRequestBuilder {

    public static ChildrenCreateRequest build(String content) {
        return new ChildrenCreateRequest(content);
    }

    public static ChildrenCreateRequest build() {
        return new ChildrenCreateRequest("생성될 대댓글 내용");
    }
}
