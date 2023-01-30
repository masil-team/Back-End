package com.masil.global.error.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(BAD_REQUEST, 1001, " Invalid Input Value"),
    ENTITY_NOT_FOUND(BAD_REQUEST, 1002, " Entity Not Found"),
    SERVER_ERROR(INTERNAL_SERVER_ERROR, 1003, "Server Error"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, 1004, "method not allowed"),
    INVALID_TYPE_VALUE(BAD_REQUEST, 1005, " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(FORBIDDEN, 1006, "Access is Denied"),

    // member
    MEMBER_NOT_FOUND(NOT_FOUND, 2001, "존재하지 않는 사용자입니다."),

    // post
    POST_NOT_FOUND(NOT_FOUND, 3001, "존재하지 않는 게시글입니다."),
    POST_ACCESS_DENIED(FORBIDDEN, 3002, "해당 게시글에 대한 권한이 없습니다"),

    // post like
    POST_NOT_SELF_LIKE(FORBIDDEN, 4001, "본인 글에는 좋아요를 누를 수 없습니다."),

    // commentLike
    COMMENT_NOT_SELF_LIKE(FORBIDDEN, 4001, "본인 댓글에는 좋아요를 누를 수 없습니다."),

    // comment
    COMMENT_NOT_FOUND(NOT_FOUND, 3001, "존재하지 않는 댓글입니다."),
    COMMENT_ACCESS_DENIED(FORBIDDEN, 3002, "해당 댓글에 대한 권한이 없습니다")

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
