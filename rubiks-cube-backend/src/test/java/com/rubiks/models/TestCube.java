package com.rubiks.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.rubiks.rubiks_cube.models.Cell;
import com.rubiks.rubiks_cube.models.Cube;
import com.rubiks.rubiks_cube.models.Operation;
import com.rubiks.rubiks_cube.models.Cell.Color;

import com.rubiks.rubiks_cube.models.Cube.Axis;;

public class TestCube {
    Cube testCube;
    HashMap<Integer, Color> colorMap;

    @BeforeEach
    void runBefore() {
        testCube = new Cube();
        colorMap = new HashMap<>();
        setColorMap();
    }

    @Test
    void testConstructor() {
        ArrayList<ArrayList<Cell>> cube = testCube.getCube();
        for (int i = 0; i < 6; i++) {
            Color color = colorMap.get(i);
            ArrayList<Cell> face = cube.get(i);
            for (Cell cell : face) {
                assertEquals(color, cell.getColor());
            }
        }
    }

    @Test
    void testXRightLower() {
        Function<Integer, Boolean> isFirstThree = (i) -> i == 0 || i == 1 || i <= 2;
        testCube.updateCellPositions(new Operation(Axis.X, 0, 0));
        testFaceWithTwoColors(0, Color.GREEN, Color.WHITE, isFirstThree);
        testFaceWithTwoColors(1, Color.WHITE, Color.BLUE, isFirstThree);
        testFaceWithTwoColors(2, Color.BLUE, Color.YELLOW, isFirstThree);
        testFaceWithTwoColors(3, Color.YELLOW, Color.GREEN, isFirstThree);
    }

    private void testFaceWithTwoColors(int position, Color c1, Color c2, Function<Integer, Boolean> checker) {
        ArrayList<Cell> face = getAnotherFace(position);
        for (int i = 0; i < 9; i++) {
            Color color = face.get(i).getColor();
            if (checker.apply(i)) {
                assertEquals(c1, color);
            } else {
                assertEquals(c2, color);
            }
        }
    }

    @Test
    void testXLeftUpper() {
        Function<Integer, Boolean> isLastThree = (i) -> i == 6 || i == 7 || i == 8;
        testCube.updateCellPositions(new Operation(Axis.X, 1, 2));
        testFaceWithTwoColors(0, Color.BLUE, Color.WHITE, isLastThree);
        testFaceWithTwoColors(1, Color.YELLOW, Color.BLUE, isLastThree);
        testFaceWithTwoColors(2, Color.GREEN, Color.YELLOW, isLastThree);
        testFaceWithTwoColors(3, Color.WHITE, Color.GREEN, isLastThree);
    }

    @Test
    void testYDownLower() {
        Function<Integer, Boolean> isLeft = (i) -> i == 0 || i == 3 || i == 6;
        testCube.updateCellPositions(new Operation(Axis.Y, 0, 0));
        testFaceWithTwoColors(1, Color.ORANGE, Color.BLUE, isLeft);
        testFaceWithTwoColors(5, Color.BLUE, Color.RED, isLeft);
        testFaceWithTwoColors(4, Color.GREEN, Color.ORANGE, isLeft);
        testFaceWithTwoColors(3, Color.RED, Color.GREEN, (i) -> i == 2 || i == 5 || i == 8);
    }

    @Test
    void testYUpDownUpper() {
        Function<Integer, Boolean> isRight = (i) -> i == 2 || i == 5 || i == 8;
        testCube.updateCellPositions(new Operation(Axis.Y, 1, 2));
        testFaceWithTwoColors(1, Color.RED, Color.BLUE, isRight);
        testFaceWithTwoColors(4, Color.BLUE, Color.ORANGE, isRight);
        testFaceWithTwoColors(5, Color.GREEN, Color.RED, isRight);
        testFaceWithTwoColors(3, Color.ORANGE, Color.GREEN, (i) -> i == 0 || i == 3 || i == 6);
    }

    @Test
    void testZCounterClock() {
        Function<Integer, Boolean> isMiddleV = (i) -> i == 1 || i == 4 || i == 7;
        Function<Integer, Boolean> isMiddleH = (i) -> i == 3 || i == 4 || i == 5;
        testCube.updateCellPositions(new Operation(Axis.Z, 1, 1));
        testFaceWithTwoColors(0, Color.ORANGE, Color.WHITE, isMiddleV);
        testFaceWithTwoColors(2, Color.RED, Color.YELLOW, isMiddleV);
        testFaceWithTwoColors(4, Color.YELLOW, Color.ORANGE, isMiddleH);
        testFaceWithTwoColors(5, Color.WHITE, Color.RED, isMiddleH);
    }

    @Test
    void testZClockLower() {
        testCube.updateCellPositions(new Operation(Axis.Z, 0, 0));
        testFaceWithTwoColors(0, Color.RED, Color.WHITE, (i) -> i == 2 || i == 5 || i == 8);
        testFaceWithTwoColors(2, Color.ORANGE, Color.YELLOW, (i) -> i == 0 || i == 3 || i == 6);
        testFaceWithTwoColors(4, Color.WHITE, Color.ORANGE, (i) -> i == 6 || i == 7 || i == 8);
        testFaceWithTwoColors(5, Color.YELLOW, Color.RED, (i) -> i == 0 || i == 1 || i == 2);
    }

    // test if the rotation of top correctly works
    // Top
    // ORANGE, ORANGE ORANGE
    // ORANGE, ORANGE ORANGE,
    // GREEN, GREEN, GREEN,

    // Bottom
    // BLUE RED RED
    // BLUE RED RED
    // BLUE RED RED

    // Left
    // GREEN GREEN RED
    // WHITE WHITE WHITE
    // WHITE WHITE WHITE

    // Right
    // ORANGE BLUE BLUE
    // YELLOW YELLOW YELLOW
    // YELLOW YELLOW YELLOW

    // Front
    // WHITE, WHITE, WHITE
    // ORANGE, BLUE, BLUE,
    // ORANGE, BLUE, BLUE

    // Back
    // YELLOW YELLOW YELLOW
    // GREEN GREEN RED
    // GREEN GREEN RED
    @Test
    void testXAndYMotion() {
        testCube.updateCellPositions(new Operation(Axis.Y, 0, 0));
        testCube.updateCellPositions(new Operation(Axis.X, 0, 0));

        testFaceWithThreeColors(1, Color.WHITE, Color.ORANGE, Color.BLUE,
                (i) -> i == 0 || i == 1 || i == 2, (i) -> i == 3 || i == 6);
        testFaceWithThreeColors(3, Color.RED, Color.YELLOW, Color.GREEN,
                (i) -> i == 5 || i == 8, (i) -> i == 0 || i == 1 || i == 2);
        testFaceWithTwoColors(4, Color.GREEN, Color.ORANGE, (i) -> i == 6 || i == 7 || i == 8);
        testFaceWithTwoColors(5, Color.BLUE, Color.RED, (i) -> i == 0 || i == 3 || i == 6);
        testFaceWithThreeColors(0, Color.RED, Color.GREEN, Color.WHITE, (i) -> i == 2,
                (i) -> i == 0 || i == 1);
        testFaceWithThreeColors(2, Color.ORANGE, Color.BLUE, Color.YELLOW, (i) -> i == 0,
                (i) -> i == 1 || i == 2);
    }

    private void testFaceWithThreeColors(int position, Color c1, Color c2, Color c3,
            Function<Integer, Boolean> checker1, Function<Integer, Boolean> checker2) {
        ArrayList<Cell> front = getAnotherFace(position);
        for (int i = 0; i < 9; i++) {
            Color color = front.get(i).getColor();
            if (checker1.apply(i)) {
                assertEquals(c1, color);
            } else if (checker2.apply(i)) {
                assertEquals(c2, color);
            } else {
                assertEquals(c3, color);
            }
        }
    }

    // Front
    // BLUE YELLOW BLUE
    // BLUE YELLOW BLUE
    // BLUE YELLOW BLUE

    // Back
    // GREEN GREEN GREEN
    // WHITE WHITE WHITE
    // GREEN GREEN GREEN

    // Left
    // WHITE WHITE RED
    // BLUE BLUE RED
    // WHITE WHITE RED

    // Right
    // ORANGE YELLOW YELLOW
    // ORANGE GREEN GREEN
    // ORANGE YELLOW YELLOW

    // Top
    // ORANGE ORANGE ORANGE
    // ORANGE ORANGE ORANGE
    // WHITE BLUE WHITE

    // Bottom
    // YELLOW, GREEN, YELLOW
    // RED RED RED
    // RED RED RED
    @Test
    void testXAndZMotion() {
        testCube.updateCellPositions(new Operation(Axis.X, 1, 1));
        testCube.updateCellPositions(new Operation(Axis.Z, 0, 0));
        testFaceWithThreeColors(0, Color.BLUE, Color.RED, Color.WHITE, (i) -> i == 3 || i == 4,
                (i) -> i == 2 || i == 5 || i == 8);
        testFaceWithThreeColors(2, Color.ORANGE, Color.GREEN, Color.YELLOW, (i) -> i == 0 || i == 3 || i == 6,
                (i) -> i == 4 || i == 5);
        testFaceWithThreeColors(4, Color.BLUE, Color.WHITE, Color.ORANGE, (i) -> i == 7,
                (i) -> i == 6 || i == 8);
        testFaceWithThreeColors(5, Color.YELLOW, Color.GREEN, Color.RED, (i) -> i == 0 || i == 2,
                (i) -> i == 1);
        testFaceWithTwoColors(1, Color.YELLOW, Color.BLUE, (i) -> i == 1 || i == 4 || i == 7);
        testFaceWithTwoColors(3, Color.WHITE, Color.GREEN, (i) -> i == 3 || i == 4 || i == 5);
    }

    // Front
    // BLUE BLUE WHITE
    // BLUE BLUE ORANGE
    // RED WHITE WHITE

    // Back
    // YELLOW GREEN GREEN
    // RED GREEN GREEN
    // YELLOW YELLOW YELLOW

    // Left
    // RED WHITE WHITE
    // RED WHITE WHITE
    // RED GREEN GREEN

    // Right
    // ORANGE ORANGE ORANGE
    // YELLOW YELLOW YELLOW
    // BLUE BLUE ORANGE

    // Top
    // WHITE WHITE GREEN
    // ORANGE ORANGE GREEN
    // ORANGE ORANGE GREEN

    // Bottom
    // YELLOW RED RED
    // YELLOW RED RED
    // BLUE BLUE BLUE
    @Test
    void testAllRotationsZero() {
        testCube.updateCellPositions(new Operation(Axis.Z, 0, 2));
        testCube.updateCellPositions(new Operation(Axis.Y, 0, 2));
        testCube.updateCellPositions(new Operation(Axis.X, 0, 2));
        testFaceWithFourColors(1, Color.WHITE, Color.ORANGE, Color.RED, Color.BLUE,
                (i) -> i == 2 || i == 7 || i == 8,
                (i) -> i == 5, (i) -> i == 6);
        testFaceWithThreeColors(3, Color.RED, Color.YELLOW, Color.GREEN, (i) -> i == 3,
                (i) -> i == 0 || i == 6 || i == 7 || i == 8);
        testFaceWithThreeColors(0, Color.GREEN, Color.RED, Color.WHITE, (i) -> i == 7 || i == 8,
                (i) -> i == 0 || i == 3 || i == 6);
        testFaceWithThreeColors(2, Color.BLUE, Color.ORANGE, Color.YELLOW, (i) -> i == 6 || i == 7,
                (i) -> i == 0 || i == 1 || i == 2 || i == 8);
        testFaceWithThreeColors(4, Color.WHITE, Color.GREEN, Color.ORANGE, (i) -> i == 0 || i == 1,
                (i) -> i == 2 || i == 5 || i == 8);
        testFaceWithThreeColors(5, Color.YELLOW, Color.BLUE, Color.RED, (i) -> i == 0 || i == 3,
                (i) -> i == 6 || i == 7 || i == 8);
    }

    private void testFaceWithFourColors(int position, Color c1, Color c2, Color c3, Color c4,
            Function<Integer, Boolean> checker1, Function<Integer, Boolean> checker2,
            Function<Integer, Boolean> checker3) {
        ArrayList<Cell> front = getAnotherFace(position);
        for (int i = 0; i < 9; i++) {
            Color color = front.get(i).getColor();
            if (checker1.apply(i)) {
                assertEquals(c1, color);
            } else if (checker2.apply(i)) {
                assertEquals(c2, color);
            } else if (checker3.apply(i)) {
                assertEquals(c3, color);
            } else {
                assertEquals(c4, color);
            }
        }
    }

    // Front
    // BLUE BLUE RED
    // BLUE BLUE RED
    // RED RED RED

    // Back
    // ORANGE GREEN GREEN
    // ORANGE GREEN GREEN
    // ORANGE WHITE WHITE

    // Left
    // ORANGE WHITE WHITE
    // ORANGE WHITE WHITE
    // BLUE BLUE WHITE

    // Right
    // YELLOW YELLOW YELLOW
    // YELLOW YELLOW YELLOW
    // YELLOW GREEN GREEN

    // Top
    // YELLOW YELLOW BLUE
    // ORANGE ORANGE BLUE
    // ORANGE ORANGE BLUE

    // Bottom
    // GREEN GREEN GREEN
    // RED RED WHITE
    // RED RED WHITE
    @Test
    void testAllRotationsOne() {
        testCube.updateCellPositions(new Operation(Axis.Z, 1, 2));
        testCube.updateCellPositions(new Operation(Axis.Y, 1, 2));
        testCube.updateCellPositions(new Operation(Axis.X, 1, 2));
        testFaceWithThreeColors(4, Color.YELLOW, Color.BLUE, Color.ORANGE, (i) -> i == 0 || i == 1,
                (i) -> i == 2 || i == 5 || i == 8);
        testFaceWithThreeColors(5, Color.WHITE, Color.GREEN, Color.RED, (i) -> i == 5 || i == 8,
                (i) -> i == 0 || i == 1 || i == 2);
        testFaceWithThreeColors(3, Color.WHITE, Color.ORANGE, Color.GREEN, (i) -> i == 7 || i == 8,
                (i) -> i == 0 || i == 3 || i == 6);
        testFaceWithTwoColors(1, Color.BLUE, Color.RED, (i) -> i == 0 || i == 1 || i == 3 || i == 4);
        testFaceWithThreeColors(0, Color.ORANGE, Color.BLUE, Color.WHITE, (i) -> i == 0 || i == 3,
                (i) -> i == 6 || i == 7);
        testFaceWithTwoColors(2, Color.GREEN, Color.YELLOW, (i) -> i == 7 || i == 8);
    }

    // Left
    // WHITE RED WHITE
    // WHITE RED WHITE
    // WHITE BLUE WHITE

    // Right
    // YELLOW ORANGE YELLOW
    // YELLOW ORANGE YELLOW
    // YELLOW GREEN YELLOW

    // Front
    // BLUE BLUE ORANGE
    // BLUE BLUE ORANGE
    // BLUE BLUE ORANGE

    // Back
    // RED GREEN GREEN
    // RED GREEN GREEN
    // RED GREEN GREEN

    // Top
    // ORANGE ORANGE GREEN
    // WHITE WHITE WHITE
    // ORANGE ORANGE GREEN

    // Bottom
    // RED RED BLUE
    // YELLOW YELLOW YELLOW
    // RED RED BLUE

    @Test
    void testYRotationUp() {
        testCube.updateCellPositions(new Operation(Axis.Y, 0, 2));
        testCube.updateCellPositions(new Operation(Axis.Z, 0, 1));
        testFaceWithTwoColors(1, Color.ORANGE, Color.BLUE, (i) -> i == 2 || i == 5 || i == 8);
        testFaceWithTwoColors(3, Color.RED, Color.GREEN, (i) -> i == 0 || i == 3 || i == 6);
        testFaceWithThreeColors(4, Color.GREEN, Color.WHITE, Color.ORANGE, (i) -> i == 2 || i == 8,
                (i) -> i == 3 || i == 4 || i == 5);
        testFaceWithThreeColors(5, Color.BLUE, Color.YELLOW, Color.RED, (i) -> i == 2 || i == 8,
                (i) -> i == 3 || i == 4 || i == 5);
        testFaceWithThreeColors(2, Color.ORANGE, Color.GREEN, Color.YELLOW,
                (i) -> i == 1 || i == 4, (i) -> i == 7);
        testFaceWithThreeColors(0, Color.RED, Color.BLUE, Color.WHITE, (i) -> i == 1 || i == 4,
                (i) -> i == 7);
    }

    private ArrayList<Cell> getAnotherFace(int position) {
        return testCube.getCube().get(position);
    }

    private void setColorMap() {
        colorMap.put(0, Color.WHITE);
        colorMap.put(1, Color.BLUE);
        colorMap.put(2, Color.YELLOW);
        colorMap.put(3, Color.GREEN);
        colorMap.put(4, Color.ORANGE);
        colorMap.put(5, Color.RED);
    }
}