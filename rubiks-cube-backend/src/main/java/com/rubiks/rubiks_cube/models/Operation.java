package com.rubiks.rubiks_cube.models;

import com.rubiks.rubiks_cube.exceptions.DirectionException;
import com.rubiks.rubiks_cube.exceptions.PositionException;
import com.rubiks.rubiks_cube.models.Cube.Axis;

public class Operation {
    private Axis axis;
    private int direction;
    private int index;

    // REQUIRES: index has to be 0 - 2, direction has to be 0 or 1
    // EFFECTS: constructs a layer operation with given axis, direction and index, and sets the type LAYER
    public Operation(Axis axis, int direction, int index) throws DirectionException, PositionException {
        if (!(direction == 0 || direction == 1)) {
            throw new DirectionException();
        }

        if (!(index >=0 && index <= 2)) {
            throw new PositionException();
        }

        this.axis = axis;
        this.direction = direction;
        this.index = index;
    }
    // EFFECTS: returns axis
    public Axis getAxis() {
        return axis;
    }

    // EFFECTS: returns index
    public int getIndex() {
        return index;
    }

    // EFFECTS: returns direction
    public int getDirection() {
        return direction;
    }

    // EFFECTS: returns complement operation
    public Operation getComplementOperation() {
        int nextDirection;
        if (direction == 0) {
            nextDirection = 1;
        } else {
            nextDirection = 0;
        }

        return new Operation(axis, nextDirection, index);
    }

}
