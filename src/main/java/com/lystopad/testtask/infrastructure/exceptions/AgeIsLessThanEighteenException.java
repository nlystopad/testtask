package com.lystopad.testtask.infrastructure.exceptions;

public class AgeIsLessThanEighteenException extends RuntimeException {
    public AgeIsLessThanEighteenException(String message) {
        super(message);
    }
}
