/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package contest_2ima20.core.boundaryembedding;

import nl.tue.geometrycore.geometry.Vector;

/**
 *
 * @author wmeulema
 */
public enum Direction {

    UP, DOWN, LEFT, RIGHT, UNDEFINED;

    public static Direction parse(int c) {
        switch (c) {
            case 'U':
                return UP;
            case 'D':
                return DOWN;
            case 'L':
                return LEFT;
            case 'R':
                return RIGHT;
            default:
                return UNDEFINED;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case UP:
                return "U";
            case DOWN:
                return "D";
            case LEFT:
                return "L";
            case RIGHT:
                return "R";
            default:
                return "X";
        }
    }

    public Vector toVector() {
        switch (this) {
            case UP:
                return Vector.up();
            case DOWN:
                return Vector.down();
            case LEFT:
                return Vector.left();
            case RIGHT:
                return Vector.right();
            default:
                return null;
        }
    }

}
