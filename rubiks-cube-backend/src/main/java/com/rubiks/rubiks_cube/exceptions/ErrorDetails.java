package com.rubiks.rubiks_cube.exceptions;

public class ErrorDetails {
    private int status;
    private String message;

    public ErrorDetails(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public int getStatusCode() {
        return status;
    }

    public void setStatusCode(int statusCode) {
        this.status = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
