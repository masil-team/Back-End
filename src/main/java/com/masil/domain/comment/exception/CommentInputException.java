package com.masil.domain.comment.exception;

import com.masil.global.error.exception.BusinessException;
import com.masil.global.error.exception.ErrorCode;

public class CommentInputException extends BusinessException {

    public CommentInputException() {
        super(ErrorCode.COMMENT_INPUT_EXCEPTION);
    }
}
