package com.masil.domain.post.exception;

import com.masil.global.error.exception.BusinessException;
import com.masil.global.error.exception.ErrorCode;

public class PostSearchInputException extends BusinessException {

    public PostSearchInputException() {
        super(ErrorCode.POST_SEARCH_INPUT_EXCEPTION);
    }
}
