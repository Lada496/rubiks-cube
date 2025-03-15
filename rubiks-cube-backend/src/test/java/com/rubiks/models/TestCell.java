package com.rubiks.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.rubiks.rubiks_cube.models.Cell;
import com.rubiks.rubiks_cube.models.Cell.Color;

public class TestCell {
    private Cell testCell;

    @BeforeEach
    void runBefore() {
        testCell = new Cell(Color.BLUE);
    }

    @Test
    void testConstructor() {
        assertEquals(Color.BLUE, testCell.getColor());
    }

    @Test
    void getColorTest() {
        assertEquals(Color.BLUE, testCell.getColor());
    }
}
