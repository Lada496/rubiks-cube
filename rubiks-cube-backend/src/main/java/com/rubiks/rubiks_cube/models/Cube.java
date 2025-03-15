package com.rubiks.rubiks_cube.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.rubiks.rubiks_cube.models.Cell.Color;

/*
 * Represents a Rubik's Cube
 * This class is responsible for all cube operations
 */
public class Cube  {
    private static final Map<Integer, Integer> Y_MAP_DOWN;

    static {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(1, 4); // blue to orange
        map.put(4, 3); // orange to green
        map.put(5, 1); // red to blue
        map.put(3, 5); // green to red

        Y_MAP_DOWN = Collections.unmodifiableMap(map);
    }

    private static final Map<Integer, Integer> Y_MAP_UP;

    static {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(1, 5);
        map.put(4, 1);
        map.put(5, 3);
        map.put(3, 4);

        Y_MAP_UP = Collections.unmodifiableMap(map);
    }

    private static final Map<Integer, Integer> Z_MAP_CLOCK;

    static {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(4, 0); // orange to white
        map.put(0, 5); // white to red
        map.put(5, 2); // red to yellow
        map.put(2, 4); // yellow to orange

        Z_MAP_CLOCK = Collections.unmodifiableMap(map);
    }

    private static final Map<Integer, Integer> Z_MAP_COUNTER_CLOCK;

    static {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(4, 2); // orange to yellow
        map.put(0, 4); // white to orange
        map.put(5, 0); // red to white
        map.put(2, 5); // yellow to red

        Z_MAP_COUNTER_CLOCK = Collections.unmodifiableMap(map);
    }

    public enum Axis {
        X,
        Y,
        Z
    }

    private ArrayList<ArrayList<Cell>> cube;

    // EFFECTS: constructs a cube by setting all faces
    public Cube() {
        ArrayList<Color> colors = new ArrayList<>();
        cube = new ArrayList<>();
        setColors(colors);
        for (int i = 0; i < 6; i++) {
            Color color = colors.get(i);
            ArrayList<Cell> face = new ArrayList<>();
            for (int j = 0; j < 9; j++) {
                face.add(new Cell(color));
            }
            cube.add(face);
        }
    }

    // EFFECTS: returns cube
    public ArrayList<ArrayList<Cell>> getCube() {
        return cube;
    }

    // MODIFIES: this
    // EFFECTS: changes the position of cells with given axis, direction and
    // position
    // direction represents:
    // - X
    // -- 0: right
    // -- 1: left
    // - Y
    // -- 0: down
    // -- 1: up
    // - Z
    // -- 0: clock
    // -- 1: counter-clock
    public void updateCellPositions(Operation operation) {
        Axis axis = operation.getAxis();
        int direction = operation.getDirection();
        int position = operation.getIndex();
        switch (axis) {
            case X:
                handleXUpdate(direction, position);
                break;
            case Y:
                handleYUpdate(direction, position);
                break;
            default:
                handleZUpdate(direction, position);
                break;
        }
    }

    // REQUIRES: direction must be 0 or 1 and position must be 0 - 2
    // MODIFIES: this
    // EFFECTS: handles whole z-axis cell updates
    private void handleZUpdate(int direction, int position) {
        if (position == 0) {
            rotation(cube.get(1), direction);
        } else if (position == 2) {
            rotation(cube.get(3), (direction == 0) ? 1 : 0);
        }
        inDepthMove(direction, position);
    }

    // REQUIRES: direction must be 0 or 1 and position must be 0 - 2
    // MODIFIES: this
    // EFFECTS: performs a z-axis move by updating face exchanges based on the given
    // position
    private void inDepthMove(int direction, int position) {
        if (direction == 0) {
            inDepthMoveClock(position);
        } else {
            inDepthMoveCounterClock(position);
        }
    }

    // REQUIRES: position must be 0 - 2
    // MODIFIES: this
    // EFFECTS: performs a clockwise z-axis move by updating face exchanges based on
    // the given position
    private void inDepthMoveClock(int position) {
        ArrayList<ArrayList<Cell>> originalCube = getCloneCube();
        ArrayList<Cell> ofWhite = originalCube.get(Z_MAP_CLOCK.get(0));
        ArrayList<Cell> ofYellow = originalCube.get(Z_MAP_CLOCK.get(2));
        ArrayList<Cell> ofOrange = originalCube.get(Z_MAP_CLOCK.get(4));
        ArrayList<Cell> ofRed = originalCube.get(Z_MAP_CLOCK.get(5));
        handleEachFaceUpdate(0, ofWhite,
                (i) -> 2 - position + i * 3,
                (i) -> position * 3 + i);
        handleEachFaceUpdate(2, ofYellow,
                (i) -> position + i * 3,
                (i) -> (6 - position * 3) + i);
        handleEachFaceUpdate(4, ofOrange,
                (i) -> (6 - position * 3) + i,
                (i) -> (8 - position) - i * 3);
        handleEachFaceUpdate(5, ofRed,
                (i) -> position * 3 + i,
                (i) -> (6 + position) - i * 3);
    }

    // REQUIRES: position must be 0 - 2
    // MODIFIES: this
    // EFFECTS: performs a counter-clockwise z-axis move by updating face exchanges
    // based on the given position
    private void inDepthMoveCounterClock(int position) {
        ArrayList<ArrayList<Cell>> originalCube = getCloneCube();
        ArrayList<Cell> ofWhite = originalCube.get(Z_MAP_COUNTER_CLOCK.get(0));
        ArrayList<Cell> ofYellow = originalCube.get(Z_MAP_COUNTER_CLOCK.get(2));
        ArrayList<Cell> ofOrange = originalCube.get(Z_MAP_COUNTER_CLOCK.get(4));
        ArrayList<Cell> ofRed = originalCube.get(Z_MAP_COUNTER_CLOCK.get(5));
        handleEachFaceUpdate(0, ofWhite,
                (i) -> 2 - position + i * 3,
                (i) -> (8 - position * 3) - i);
        handleEachFaceUpdate(2, ofYellow,
                (i) -> position + i * 3,
                (i) -> (position * 3 + 2) - i);
        handleEachFaceUpdate(4, ofOrange,
                (i) -> (6 - position * 3) + i,
                (i) -> position + i * 3);
        handleEachFaceUpdate(5, ofRed,
                (i) -> position * 3 + i,
                (i) -> 2 - position + i * 3);
    }

    // REQUIRES: faceIndexToUpdate must be 0 - 6
    // MODIFIES: this
    // EFFECTS: updates the specified face of the cube by copying cells from
    // originalFace
    private void handleEachFaceUpdate(
            int faceIndexToUpdate,
            ArrayList<Cell> originalFace,
            Function<Integer, Integer> getIndexToChangeFn,
            Function<Integer, Integer> getOriginalIndexFn) {
        for (int i = 0; i < 3; i++) {
            int indexToChange = getIndexToChangeFn.apply(i);
            int originalIndex = getOriginalIndexFn.apply(i);
            Cell cell = originalFace.get(originalIndex);
            cube.get(faceIndexToUpdate).set(indexToChange, cell);
        }
    }

    // REQUIRES: direction must be 0 or 1 and position must be 0 - 2
    // MODIFIES: this
    // EFFECTS: handles whole y-axis cell updates
    private void handleYUpdate(int direction, int position) {
        if (position == 0) {
            rotation(cube.get(0), direction);
        } else if (position == 2) {
            rotation(cube.get(2), (direction == 0) ? 1 : 0);
        }
        verticalMove(direction, position);
    }

    // REQUIRES: direction must be 0 or 1 and position must be 0 - 2
    // MODIFIES: this
    // EFFECTS: performs a y-axis move by updating face exchanges based on the given
    // position
    private void verticalMove(int direction, int position) {
        if (direction == 0) {
            verticalDown(position);
        } else {
            verticalUp(position);
        }
    }

    // REQUIRES: position must be 0 - 2
    // MODIFIES: this
    // EFFECTS: performs a upward y-axis move by updating face exchanges based on
    // the given position
    private void verticalUp(int position) {
        ArrayList<ArrayList<Cell>> originalCube = getCloneCube();
        Function<Integer, Integer> getIndexToChangeFn = (j) -> j * 3 + position;
        Function<Integer, Integer> getOriginalIndexFn = (j) -> j * 3 + position;

        for (int i = 0; i < 6; i++) {
            if (Y_MAP_UP.containsKey(i)) {
                int originalFaceIndex = Y_MAP_UP.get(i);
                ArrayList<Cell> originalFace = originalCube.get(originalFaceIndex);
                if (i == 3) {
                    handleEachFaceUpdate(i, originalFace,
                            (j) -> j * 3 + 2 - position,
                            (j) -> 6 + position - j * 3);
                } else if (i == 5) {
                    handleEachFaceUpdate(i, originalFace, getIndexToChangeFn,
                            (j) -> 8 - position - j * 3);
                } else {
                    handleEachFaceUpdate(i, originalFace, getIndexToChangeFn, getOriginalIndexFn);
                }
            }
        }
    }

    // REQUIRES: position must be 0 - 2
    // MODIFIES: this
    // EFFECTS: performs a downward y-axis move by updating face exchanges based on
    // the given position
    private void verticalDown(int position) {
        ArrayList<ArrayList<Cell>> originalCube = getCloneCube();
        Function<Integer, Integer> getIndexToChangeFn = (j) -> j * 3 + position;
        Function<Integer, Integer> getOriginalIndexFn = (j) -> j * 3 + position;
        for (int i = 0; i < 6; i++) {
            if (Y_MAP_DOWN.containsKey(i)) {
                int originalFaceIndex = Y_MAP_DOWN.get(i);
                ArrayList<Cell> originalFace = originalCube.get(originalFaceIndex);
                if (i == 3) {
                    handleEachFaceUpdate(i, originalFace,
                            (j) -> (2 - position) + j * 3,
                            (j) -> 6 + position - j * 3);
                } else if (i == 4) {
                    handleEachFaceUpdate(i, originalFace, getIndexToChangeFn,
                            (j) -> (8 - position) - j * 3);
                } else {
                    handleEachFaceUpdate(i, originalFace, getIndexToChangeFn, getOriginalIndexFn);
                }
            }
        }
    }

    // REQUIRES: direction must be 0 or 1 and position must be 0 - 2
    // MODIFIES: this
    // EFFECTS: handles whole x-axis cell updates
    private void handleXUpdate(int direction, int position) {
        // top -> rotation
        if (position == 0) {
            rotation(cube.get(4), (direction == 1) ? 0 : 1);
            // bottom -> rotation
        } else if (position == 2) {
            rotation(cube.get(5), direction);
        }

        horizontalMove(direction, position);
    }

    // REQUIRES: position must be 0 - 2
    // MODIFIES: this
    // EFFECTS: performs a x-axis move by updating face exchanges based on the given
    // position
    private void horizontalMove(int direction, int position) {
        ArrayList<ArrayList<Cell>> originalCube = getCloneCube();
        for (int i = 0; i < 4; i++) {
            int indexFaceOriginal = (direction == 0) ? (i + 3) % 4 : (i + 1) % 4;
            ArrayList<Cell> originalFace = originalCube.get(indexFaceOriginal);

            for (int j = 0; j < 3; j++) {
                int index = position * 3 + j;
                Cell cell = originalFace.get(index);
                cube.get(i).set(index, cell);
            }
        }
    }

    // REQUIRES: direction must be 0 or 1
    // MODIFIES: this
    // EFFECTS: performs a face rotational cell updates
    private void rotation(ArrayList<Cell> face, int direction) {
        ArrayList<Cell> temp = new ArrayList<>(face);
        for (int i = 0; i < face.size(); i++) {
            int floorDiv = i / 3;
            int base = getBase(direction, floorDiv);
            int nextIndex = getNextIndex(i, base, direction);
            face.set(i, temp.get(nextIndex));
        }
    }

    // REQUIRES: direction must be 0 or 1
    // EFFECTS: returns a base number used to calculate the index of a cell to copy
    private int getBase(int direction, int floorDiv) {
        int base;
        if (direction == 0) {
            base = 6 + floorDiv;
        } else {
            base = 2 - floorDiv;
        }
        return base;
    }

    // REQUIRES: index must be 0 - 8 and direction must be 0 or 1
    // EFFECTS: returns the index of a cell to copy from the original face to place
    // it at the given index
    private int getNextIndex(int index, int base, int direction) {
        if (index % 3 == 1) {
            return (direction == 0) ? base - 3 : base + 3;
        } else if (index % 3 == 2) {
            return (direction == 0) ? base - 6 : base + 6;
        }
        return base;
    }

    // EFFECTS: returns the copied cube
    private ArrayList<ArrayList<Cell>> getCloneCube() {
        ArrayList<ArrayList<Cell>> originalCube = new ArrayList<>();

        for (ArrayList<Cell> face : cube) {
            ArrayList<Cell> copiedFace = new ArrayList<>();
            for (Cell cell : face) {
                copiedFace.add(cell);
            }
            originalCube.add(copiedFace);
        }
        return originalCube;
    }

    // EFFECTS: add colors to this.colors in this order
    private void setColors(ArrayList<Color> colors) {
        colors.add(Color.WHITE);
        colors.add(Color.BLUE);
        colors.add(Color.YELLOW);
        colors.add(Color.GREEN);
        colors.add(Color.ORANGE);
        colors.add(Color.RED);
    }
}