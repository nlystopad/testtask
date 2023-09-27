package com.lystopad.testtask.infrastructure.exceptions;

public class DateFromIsMoreThanTo extends RuntimeException{
    public DateFromIsMoreThanTo(String message) {
        super(message);
    }
}
