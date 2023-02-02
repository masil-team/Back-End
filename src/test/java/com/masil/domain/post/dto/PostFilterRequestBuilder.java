package com.masil.domain.post.dto;

import org.springframework.data.domain.PageRequest;

import static org.springframework.data.domain.Sort.Direction.DESC;

public class PostFilterRequestBuilder {

    public static PostFilterRequest build() {
        return new PostFilterRequest(1L, 11110111, PageRequest.of(0, 8, DESC, "createDate"));
    }
}
