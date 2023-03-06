package com.masil.domain.post.dto;


import java.util.ArrayList;
import java.util.List;

public class PostCreateRequestBuilder {

    public static PostCreateRequest build(String content, Long boardId, List<Long> fileIds) {
        return new PostCreateRequest(content, boardId, fileIds);
    }

    public static PostCreateRequest build(String content, Long boardId) {
        List<Long> fileIds = new ArrayList<>();
        fileIds.add(1L);
        return new PostCreateRequest(content, boardId, fileIds);
    }

    public static PostCreateRequest build(String content) {
        List<Long> fileIds = new ArrayList<>();
        fileIds.add(1L);
        return new PostCreateRequest(content, 1L, fileIds);
    }

    public static PostCreateRequest build() {
        List<Long> fileIds = new ArrayList<>();
        fileIds.add(1L);
        fileIds.add(2L);
        fileIds.add(3L);
        return new PostCreateRequest("생성될 내용", 1L, fileIds);
    }

}
