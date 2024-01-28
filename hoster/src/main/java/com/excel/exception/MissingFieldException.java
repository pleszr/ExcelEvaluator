
package com.excel.exception;

import java.util.UUID;

public class MissingFieldException extends RuntimeException {
    private final String exceptionId;

    public MissingFieldException() {
        super();
        this.exceptionId = UUID.randomUUID().toString();
    }
    public MissingFieldException(String message) {
        super(message);
        this.exceptionId = UUID.randomUUID().toString();

    }


    public String getExceptionId() {
        return exceptionId;
    }

}

