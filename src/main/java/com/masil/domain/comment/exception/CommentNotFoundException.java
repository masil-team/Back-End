package com.masil.domain.comment.exception;

public class CommentNotFoundException extends RuntimeException {
    private static final String MESSAGE = "댓글을 찾을 수 없습니다.";

    public CommentNotFoundException() {
        super(MESSAGE);
    }
}
