package com.masil.domain.post.exception;

import com.masil.global.error.exception.ErrorCode;
import com.masil.global.error.exception.NoAuthenticationException;

public class PostAccessDeniedException extends NoAuthenticationException {
    public PostAccessDeniedException() {
        super(ErrorCode.POST_ACCESS_DENIED);
    }
}
