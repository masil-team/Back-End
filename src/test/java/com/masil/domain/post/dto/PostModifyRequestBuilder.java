package com.masil.domain.post.dto;

public class PostModifyRequestBuilder {

    public static PostModifyRequest build(String content, Long boardId) {
        return new PostModifyRequest(content, boardId);
    }
    public static PostModifyRequest build(String content) {
        return new PostModifyRequest(content, 1L);
    }

    public static PostModifyRequest build() {
        return new PostModifyRequest("수정할 내용", 1L);
    }
}
