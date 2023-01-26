package com.masil.domain.postlike.exception;

import com.masil.global.error.exception.ErrorCode;
import com.masil.global.error.exception.NoAuthenticationException;

public class SelfPostLikeException extends NoAuthenticationException {
    public SelfPostLikeException() {
        super(ErrorCode.POST_NOT_SELF_LIKE);
    }
}
