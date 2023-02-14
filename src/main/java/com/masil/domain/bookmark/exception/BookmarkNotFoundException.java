package com.masil.domain.bookmark.exception;

import com.masil.global.error.exception.EntityNotFoundException;
import com.masil.global.error.exception.ErrorCode;

public class BookmarkNotFoundException extends EntityNotFoundException {

    public BookmarkNotFoundException() {
        super(ErrorCode.BOOKMARK_NOT_FOUND);
    }
}
