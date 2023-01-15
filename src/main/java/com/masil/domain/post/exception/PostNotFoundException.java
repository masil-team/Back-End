package com.masil.domain.post.exception;

public class PostNotFoundException extends RuntimeException {

    private static final String MESSAGE = "게시물을 찾을 수 없습니다.";

    public PostNotFoundException() {
        super(MESSAGE);
    }
}
