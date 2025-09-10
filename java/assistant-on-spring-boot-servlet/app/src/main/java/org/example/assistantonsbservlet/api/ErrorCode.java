package org.example.assistantonsbservlet.api;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    UNKNOWN(-1, HttpStatus.INTERNAL_SERVER_ERROR),
    ENTITY_NOT_FOUND(1, HttpStatus.NOT_FOUND),
    INVALID_INPUT(2, HttpStatus.BAD_REQUEST);

    private final int code;
    private final HttpStatus httpStatus;

    ErrorCode(int code, HttpStatus httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public int getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
