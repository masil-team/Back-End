package com.masil.domain.post.dto;


public class PostCreateRequestBuilder {

    public static PostCreateRequest build(String content, Long boardId) {
        return new PostCreateRequest(content, boardId);
    }

    public static PostCreateRequest build(String content) {
        return new PostCreateRequest(content, 1L);
    }

    public static PostCreateRequest build() {
        return new PostCreateRequest("생성될 내용", 1L);
    }

}
