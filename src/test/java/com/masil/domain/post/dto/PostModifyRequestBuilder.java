package com.masil.domain.post.dto;

public class PostModifyRequestBuilder {
    public static PostModifyRequest build(String content) {
        return new PostModifyRequest(content);
    }

    public static PostModifyRequest build() {
        return new PostModifyRequest("수정할 내용");
    }
}
