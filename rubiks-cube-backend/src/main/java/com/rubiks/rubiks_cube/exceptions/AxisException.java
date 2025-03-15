package com.rubiks.rubiks_cube.exceptions;

public class AxisException extends OperationException {
    public AxisException() {
        super("Invalid axis request");
    }
}
