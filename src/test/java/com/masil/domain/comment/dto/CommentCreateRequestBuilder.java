package com.masil.domain.comment.dto;

import com.masil.domain.post.dto.PostCreateRequest;

public class CommentCreateRequestBuilder {

        public static CommentCreateRequest build(String content) {
            return new CommentCreateRequest(content);
        }

        public static CommentCreateRequest build() {
            return new CommentCreateRequest("생성될 내용");
        }

}
