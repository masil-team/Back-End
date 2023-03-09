package com.masil.domain.storage.exception;

import com.masil.global.error.exception.EntityNotFoundException;
import com.masil.global.error.exception.ErrorCode;

public class FileNotFoundException extends EntityNotFoundException {

    public FileNotFoundException() {
        super(ErrorCode.FILE_NOT_FOUND);
    }
}
