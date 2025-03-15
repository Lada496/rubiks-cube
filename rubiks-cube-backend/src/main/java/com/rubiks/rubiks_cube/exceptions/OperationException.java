package com.rubiks.rubiks_cube.exceptions;

public class OperationException extends RuntimeException {
    private String message;
    public OperationException(String message) {
        super(message);
    }

    public OperationException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
