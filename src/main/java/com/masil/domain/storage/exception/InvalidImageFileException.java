package com.masil.domain.storage.exception;

import com.masil.global.error.exception.ErrorCode;
import com.masil.global.error.exception.InvalidValueException;

public class InvalidImageFileException extends InvalidValueException {

    public InvalidImageFileException() {
        super(ErrorCode.INVALID_IMAGE_FILE);
    }
}
