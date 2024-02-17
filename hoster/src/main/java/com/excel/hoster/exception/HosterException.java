package com.excel.hoster.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class HosterException extends RuntimeException {
    ErrorCode errorCode;
    @Getter
    HttpStatus httpStatus;
    public HosterException(
            ErrorCode errorCode,
            String message
    ) {
        super(message);
        this.errorCode = errorCode;
        setHttpStatus();
    }
    public String getErrorCode() {
        return errorCode.toString();
    }

    private void setHttpStatus() {
        if (errorCode.toString().equals("NOT_FOUND")) {
            this.httpStatus = HttpStatus.NOT_FOUND;
        } else {
            this.httpStatus = HttpStatus.BAD_REQUEST;
        }
    }
}


