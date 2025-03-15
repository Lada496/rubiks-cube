package com.rubiks.rubiks_cube.exceptions;

public class DirectionException extends OperationException {
    public DirectionException() {
        super("Invalid direction request");
    }
}
