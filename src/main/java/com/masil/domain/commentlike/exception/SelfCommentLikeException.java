package com.masil.domain.commentlike.exception;

import com.masil.global.error.exception.ErrorCode;
import com.masil.global.error.exception.NoAuthenticationException;

public class SelfCommentLikeException extends NoAuthenticationException {
    public SelfCommentLikeException() {
        super(ErrorCode.COMMENT_NOT_SELF_LIKE);
    }
}
