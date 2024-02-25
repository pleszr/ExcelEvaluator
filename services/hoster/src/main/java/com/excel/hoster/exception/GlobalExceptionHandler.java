package com.excel.hoster.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import java.util.UUID;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final String exceptionId;

    GlobalExceptionHandler() {
        this.exceptionId = UUID.randomUUID().toString();
    }

    @ExceptionHandler(HosterException.class)
    ProblemDetail handleHosterException(HosterException hosterException) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(hosterException.getHttpStatus(), hosterException.getMessage());
        problemDetail.setProperty("exceptionId", exceptionId);
        problemDetail.setProperty("errorCode", hosterException.getErrorCode());
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        BindingResult result = methodArgumentNotValidException.getBindingResult();

        String errorMessage = result.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errorMessage);
        problemDetail.setProperty("exceptionId", exceptionId);
        problemDetail.setProperty("errorCode", ErrorCode.MISSING_FIELD);
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
        ProblemDetail handleGenericException(Exception exception) {

        Throwable rootCause = exception.getCause();
        while (rootCause != null && rootCause.getCause() != null && rootCause != rootCause.getCause()) {
            rootCause = rootCause.getCause();
        }
        String message = (rootCause != null && rootCause.getCause() != null) ? rootCause.getMessage() : exception.getMessage();

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, message);
        problemDetail.setTitle("Internal server error");
        problemDetail.setProperty("exceptionId", exceptionId);
        return problemDetail;
    }




}



