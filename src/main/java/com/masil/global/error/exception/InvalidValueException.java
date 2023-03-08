package com.masil.global.error.exception;

public class InvalidValueException extends BusinessException{
    public InvalidValueException(String value) {
        super(value, ErrorCode.INVALID_INPUT_VALUE);
    }

    public InvalidValueException(ErrorCode errorCode) {
        super(errorCode);
    }
}
