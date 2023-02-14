package com.masil.domain.bookmark.exception;

import com.masil.global.error.exception.BusinessException;
import com.masil.global.error.exception.ErrorCode;

public class BookmarkAlreadyExistsException extends BusinessException {
    public BookmarkAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
