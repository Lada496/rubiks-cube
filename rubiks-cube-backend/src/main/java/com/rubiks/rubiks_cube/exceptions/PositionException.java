package com.rubiks.rubiks_cube.exceptions;

public class PositionException extends OperationException {
    public PositionException() {
        super("Invalid position request");
    }
}
