package com.masil.domain.post.dto;


public class PostCreateRequestBuilder {

    public static PostCreateRequest build(String content) {
        return new PostCreateRequest(content);
    }

    public static PostCreateRequest build() {
        return new PostCreateRequest("생성될 내용");
    }

}
