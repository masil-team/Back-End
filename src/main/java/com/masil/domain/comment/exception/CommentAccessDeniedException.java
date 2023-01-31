package com.masil.domain.comment.exception;

import com.masil.global.error.exception.ErrorCode;
import com.masil.global.error.exception.NoAuthenticationException;

public class CommentAccessDeniedException extends NoAuthenticationException {
    public CommentAccessDeniedException() {
        super(ErrorCode.COMMENT_ACCESS_DENIED);
    }
}
