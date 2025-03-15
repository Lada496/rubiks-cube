package com.rubiks.rubiks_cube.services;

import java.util.ArrayList;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rubiks.rubiks_cube.exceptions.AxisException;
import com.rubiks.rubiks_cube.exceptions.OperationException;
import com.rubiks.rubiks_cube.models.Cell;
import com.rubiks.rubiks_cube.models.Cube;
import com.rubiks.rubiks_cube.models.Cube.Axis;
import com.rubiks.rubiks_cube.models.Operation;

@Service
public class CubeService {
    private Stack<Operation> redoStack;
    private Stack<Operation> undoStack;
    private Cube cube;

    @Autowired
    // EFFECTS: constructs a cube with empty list as history, a cube, and empty redo
    // stack
    public CubeService () {
        cube = new Cube();
        redoStack = new Stack<>();
        undoStack = new Stack<>();
    }

    // EFFECTS: returns redoStack (for test)
    public Stack<Operation> getRedoStack() {
        return redoStack;
    }

    // EFFECTS: returns undoStack (for test)
    public Stack<Operation> getUndoStack() {
        return undoStack;
    }

    // EFFECTS: returns current cube state
    public ArrayList<ArrayList<Cell>> getCube() {
        return cube.getCube();
    }

    // MODIFIES: this
    // EFFECTS: moves cube with the given operation and updates history to add the
    // given operation and return new cube It also update undoStack. 
    public void operate(Operation operation) throws AxisException {

        if (operation.getAxis() == null || 
            (!operation.getAxis().equals(Axis.X) && 
             !operation.getAxis().equals(Axis.Y) && 
             !operation.getAxis().equals(Axis.Z))) {
            throw new AxisException();
        }

        cube.updateCellPositions(operation);
        undoStack.push(operation);
    }


    // MODIFIES: this
    // EFFECTS: performs a undo and return new cube
    public void undo() throws OperationException {
        if(undoStack.size() <= 0) {
            throw new OperationException("You can't undo without previous operations");
        }

        Operation undid = undoStack.pop();
        Operation complement = undid.getComplementOperation();
        cube.updateCellPositions(complement);

        redoStack.push(undid);
    }

    // MODIFIES: this
    // EFFECTS: performs redo and return new cube
    public void redo() throws OperationException {
        if(redoStack.size() <= 0) {
            throw new OperationException("You can't redo without undoing operation");
        }

        Operation redid = redoStack.pop();
        cube.updateCellPositions(redid);

        undoStack.add(redid);
    }   
}
