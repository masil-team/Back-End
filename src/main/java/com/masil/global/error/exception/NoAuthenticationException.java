package com.masil.global.error.exception;

public class NoAuthenticationException extends BusinessException{

    public NoAuthenticationException(String message) {
        super(message, ErrorCode.HANDLE_ACCESS_DENIED);
    }

    public NoAuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
