package com.masil.global.error.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(BAD_REQUEST, 1001, " Invalid Input Value"),
    ENTITY_NOT_FOUND(BAD_REQUEST, 1002, " Entity Not Found"),
    SERVER_ERROR(INTERNAL_SERVER_ERROR, 1003, "Server Error"),
    INVALID_TYPE_VALUE(BAD_REQUEST, 1004, " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(FORBIDDEN, 1005, "Access is Denied"),

    ;

    private HttpStatus status;
    private final int code;
    private final String message;

    ErrorCode(final HttpStatus status, final int code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public int getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }

}
