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
    DUPLICATE_EMAIL(BAD_REQUEST, 2002, "중복된 이메일 입니다."),
    DUPLICATE_NICKNAME(BAD_REQUEST, 2003, "중복된 닉네임입니다."),
    NOT_SAME_PASSWORD_CONFIRM(BAD_REQUEST, 2003, "비밀번호와 비밀번호 확인 값이 다릅니다."),
    // post
    POST_NOT_FOUND(NOT_FOUND, 3001, "존재하지 않는 게시글입니다."),
    POST_ACCESS_DENIED(FORBIDDEN, 3002, "해당 게시글에 대한 권한이 없습니다"),

    // post like
    POST_NOT_SELF_LIKE(FORBIDDEN, 4001, "본인 글에는 좋아요를 누를 수 없습니다."),

    // bookmark
    BOOKMARK_ALREADY_EXISTS(BAD_REQUEST, 3301, "잘못된 요청입니다."),
    BOOKMARK_NOT_FOUND(NOT_FOUND, 3302, "등록되지 않은 즐겨찾기입니다."),

    // commentLike
    COMMENT_NOT_SELF_LIKE(FORBIDDEN, 6001, "본인 댓글에는 좋아요를 누를 수 없습니다."),

    // comment
    COMMENT_NOT_FOUND(NOT_FOUND, 5001, "존재하지 않는 댓글입니다."),
    COMMENT_ACCESS_DENIED(FORBIDDEN, 5002, "해당 댓글에 대한 권한이 없습니다"),
    COMMENT_INPUT_EXCEPTION(LENGTH_REQUIRED, 5003, "댓글은 250자를 넘길 수 없습니다."),

    // board
    BOARD_NOT_FOUND(NOT_FOUND, 9001, "존재하지 않는 카테고리입니다."),

    // address
    INVALID_ADDRESS_SEARCH_KEYWORD(BAD_REQUEST, 8001, "잘못된 검색어 입니다."),

    // auth
    ACCESS_TOKEN_EXPIRED(BAD_REQUEST,6001,"액세스 토큰 유효기간이 지났습니다."),
    INVALID_TOKEN(BAD_REQUEST, 6002, "잘못된 토큰입니다."),
    INVALID_REFRESH_TOKEN(BAD_REQUEST,6003,"잘못된 리프레쉬 토큰입니다"),
    REFRESH_TOKEN_EXPIRED(BAD_REQUEST, 6005, "리프레쉬 토큰 유효기간이 지났습니다."),

    UNAUTHENTICATED_LOGIN_USER(UNAUTHORIZED,6004,"로그인 유저가 존재하지 않습니다.");


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
