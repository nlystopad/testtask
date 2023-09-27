package com.lystopad.testtask.infrastructure.exceptions;

import java.util.Date;

public class ProblemDetails {
    private String description;
    private String message;
    private Date timestamp;

    public ProblemDetails(String description, String message, Date timestamp) {
        super();
        this.description = description;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
