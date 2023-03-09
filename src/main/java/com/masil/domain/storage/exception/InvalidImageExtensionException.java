package com.masil.domain.storage.exception;

import com.masil.global.error.exception.ErrorCode;
import com.masil.global.error.exception.InvalidValueException;

public class InvalidImageExtensionException extends InvalidValueException {

    public InvalidImageExtensionException() {
        super(ErrorCode.INVALID_IMAGE_EXTENSION);
    }
}
