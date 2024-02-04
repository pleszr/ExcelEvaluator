package com.excel.hoster.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingFieldException.class)
    ProblemDetail handleMissingFieldException(MissingFieldException missingFieldException) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, missingFieldException.getMessage());
        problemDetail.setTitle("Missing field error");
        problemDetail.setProperty("exceptionId", missingFieldException.getExceptionId());
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        BindingResult result = methodArgumentNotValidException.getBindingResult();

        String errorMessage = result.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errorMessage);
        problemDetail.setTitle("Missing field error");
        return problemDetail;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    ProblemDetail handleMissingServletRequestParameterException(MissingServletRequestParameterException missingServletRequestParameterException) {
        ProblemDetail problemDetail = missingServletRequestParameterException.getBody();
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
        return problemDetail;
    }




}



