package com.excel.hoster.exception;

public class HosterException extends RuntimeException {
    public HosterException(
            ErrorCode errorCode,
            String message
    ) {
        super(message);
    }
}

enum ErrorCode {
    INVALID_REQUEST,
    INTERNAL_SERVER_ERROR,
    NOT_FOUND,
    USER_CURRENCY_NOT_FOUND,
}
