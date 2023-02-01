package com.masil.domain.comment.exception;

import com.masil.global.error.exception.EntityNotFoundException;
import com.masil.global.error.exception.ErrorCode;

public class CommentNotFoundException extends EntityNotFoundException {
    public CommentNotFoundException() {
        super(ErrorCode.COMMENT_NOT_FOUND);
    }
}
