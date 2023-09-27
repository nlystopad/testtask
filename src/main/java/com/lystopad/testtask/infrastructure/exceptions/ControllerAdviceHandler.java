package com.lystopad.testtask.infrastructure.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class ControllerAdviceHandler {
    @ExceptionHandler(AgeIsLessThanEighteenException.class)
    public ResponseEntity handleAgeIsLessThanEighteenException(AgeIsLessThanEighteenException exception,
                                                               WebRequest request) {
        ProblemDetails details = new ProblemDetails(request.getDescription(false), exception.getMessage(), new Date());
        return new ResponseEntity(details, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DateFromIsMoreThanTo.class)
    public ResponseEntity handleDateFromIsMoreThanToException(DateFromIsMoreThanTo exception,
                                                               WebRequest request) {
        ProblemDetails details = new ProblemDetails(request.getDescription(false), exception.getMessage(), new Date());
        return new ResponseEntity(details, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity handleUserNotFoundException(UserNotFoundException exception,
                                                      WebRequest request) {
        ProblemDetails details = new ProblemDetails(request.getDescription(false), exception.getMessage(), new Date());
        return new ResponseEntity(details, HttpStatus.NOT_FOUND);
    }
}
