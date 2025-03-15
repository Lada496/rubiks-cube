package com.rubiks.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Stack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.rubiks.rubiks_cube.models.Cube.Axis;
import com.rubiks.rubiks_cube.exceptions.OperationException;
import com.rubiks.rubiks_cube.models.Operation;
import com.rubiks.rubiks_cube.services.CubeService;

public class TestCubeService {
    private CubeService cubeService;
    private Operation operation1;
    private Operation operation2;

    @BeforeEach
    void runBefore() {
        cubeService = new CubeService();
        operation1 = new Operation(Axis.X, 0, 0);
        operation2 = new Operation(Axis.Y, 0, 0);
    }

    @Test
    void testConstructor() {
        assertEquals(0, cubeService.getRedoStack().size());
        assertEquals(0, cubeService.getUndoStack().size());
    }

    @Test
    void testOneOperation() {
        cubeService.operate(operation1);
        assertEquals(0, cubeService.getRedoStack().size());
        assertEquals(1, cubeService.getUndoStack().size());
    }

    @Test
    void testMultipleOperation() {
        cubeService.operate(operation1);
        cubeService.operate(operation2);
        assertEquals(0, cubeService.getRedoStack().size());
        assertEquals(2, cubeService.getUndoStack().size());
    }

    @Test
    void testUndoError() {
        try {
            cubeService.undo();
            fail("This line shouldn't run");
        } catch (OperationException e) {
            // pass
        }
    }

    @Test
    void testUndoOnce() {
        cubeService.operate(operation1);
        try {
            cubeService.undo();
            assertEquals(1, cubeService.getRedoStack().size());
            assertEquals(0, cubeService.getUndoStack().size());

        } catch (OperationException e) {
            fail();
        }
    }

    @Test
    void testUndoMultiple() {
        cubeService.operate(operation1);
        cubeService.operate(operation2);

        try {
            cubeService.undo();
            cubeService.undo();
            assertEquals(2, cubeService.getRedoStack().size());
            assertEquals(0, cubeService.getUndoStack().size());
        } catch (OperationException e) {
            fail();
        }
    }

    @Test
    void testRedo() {
        cubeService.operate(operation1);
        cubeService.operate(operation2);
        try {
            cubeService.undo();
            cubeService.undo();
            cubeService.redo();
            Stack<Operation> redoes = cubeService.getRedoStack();
            Stack<Operation> undoes = cubeService.getUndoStack();
            assertEquals(1, redoes.size());
            assertEquals(1, undoes.size());
            assertEquals(Axis.X, undoes.get(0).getAxis());
            assertEquals(Axis.Y, redoes.get(0).getAxis());
        } catch (OperationException e) {
            fail();
        }
    }



    @Test 
    void testRedoError() {
        try {
            cubeService.redo();
            fail("This line shouldn't run");
        } catch (OperationException e) {
            // pass
        }
    }


}
