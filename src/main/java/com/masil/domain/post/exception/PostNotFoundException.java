package com.masil.domain.post.exception;

import com.masil.global.error.exception.EntityNotFoundException;
import com.masil.global.error.exception.ErrorCode;

public class PostNotFoundException extends EntityNotFoundException {

    public PostNotFoundException() {
        super(ErrorCode.POST_NOT_FOUND);
    }
}
