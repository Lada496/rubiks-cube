package com.rubiks.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.rubiks.rubiks_cube.exceptions.DirectionException;
import com.rubiks.rubiks_cube.exceptions.OperationException;
import com.rubiks.rubiks_cube.exceptions.PositionException;
import com.rubiks.rubiks_cube.models.Operation;
import com.rubiks.rubiks_cube.models.Cube.Axis;

public class TestOperation {
    private Operation testOperationXRight;
    private Operation testOperationXLeft;
    private Operation testOperationYDown;
    private Operation testOperationZCounterClock;

    @BeforeEach
    void runBefore() {
        testOperationXRight = new Operation(Axis.X, 0, 0);
        testOperationXLeft = new Operation(Axis.X, 1, 0);
        testOperationYDown = new Operation(Axis.Y, 0, 1);
        testOperationZCounterClock = new Operation(Axis.Z, 1, 2);
    }

    @Test
    void testIllegalInput() {
        try {
            new Operation(Axis.X, -1, 0);
            fail("shouldn't run this line");
        } catch (DirectionException e) {
            // pass
        }  catch (OperationException e) {
            fail(e.getMessage());
        }

        try {
            new Operation(Axis.X, 0, -1);
            fail("shouldn't run this line");
        } catch (PositionException e) {
            // pass
        } catch (OperationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testConstructor() {
        assertEquals(Axis.X, testOperationXLeft.getAxis());
        assertEquals(1, testOperationXLeft.getDirection());
        assertEquals(0, testOperationXLeft.getIndex());
    }

    @Test
    void testGetRedoOperationX() {
        Operation redo = testOperationXRight.getComplementOperation();
        assertEquals(0, redo.getIndex());
        assertEquals(1, redo.getDirection());
        assertEquals(Axis.X, redo.getAxis());
    }

    @Test
    void testGetRedoOperationY() {
        Operation redo = testOperationYDown.getComplementOperation();
        assertEquals(1, redo.getIndex());
        assertEquals(1, redo.getDirection());
        assertEquals(Axis.Y, redo.getAxis());
    }

    @Test
    void testGetRedoOperationZ() {
        Operation redo = testOperationZCounterClock.getComplementOperation();
        assertEquals(2, redo.getIndex());
        assertEquals(0, redo.getDirection());
        assertEquals(Axis.Z, redo.getAxis());
    }

}
