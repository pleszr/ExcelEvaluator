
package com.excel.hoster.exception;

import java.util.UUID;

public class MissingFieldException extends RuntimeException {

    public MissingFieldException() {
        super();
    }
    public MissingFieldException(String message) {
        super(message);

    }

}

