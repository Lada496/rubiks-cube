package com.rubiks.rubiks_cube.models;

/**
 * Represents a cell of a Rubik's Cube
 */

public class Cell {
    public enum Color {
        RED,
        GREEN,
        BLUE,
        YELLOW,
        WHITE,
        ORANGE
    }

    private Color color;

    // EFFECTS: constructs a cell with the given color
    public Cell(Color color) {
        this.color = color;
    }

    // EFFECTS: returns the color of the cell
    public Color getColor() {
        return color;
    }
}