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
public class GridPoint extends Vector {

    public GridPoint(int x, int y) {
        super(x, y);
    }

    public GridPoint(GridPoint gp) {
        super(gp.getX(), gp.getY());
    }

    public int getIntX() {
        return (int) Math.round(getX());
    }

    public int getIntY() {
        return (int) Math.round(getY());
    }

    public boolean isSameCoordinate(GridPoint v) {
        return getIntX() == v.getIntX() && getIntY() == v.getIntY();
    }

    public boolean isAdjacentCoordinate(GridPoint v) {
        return Math.abs(getIntX() - v.getIntX()) + Math.abs(getIntY() - v.getIntY()) == 1;
    }

    public Direction getDirectionTo(GridPoint v) {
        if (!isAdjacentCoordinate(v)) {
            return Direction.UNDEFINED;
        }
        if (v.getIntX() - getIntX() < 0) {
            return Direction.LEFT;
        } else if (v.getIntX() - getIntX() > 0) {
            return Direction.RIGHT;
        } else if (v.getIntY() - getIntY() < 0) {
            return Direction.DOWN;
        } else {
            return Direction.UP;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof GridPoint)) {
            return false;
        }
        return isSameCoordinate((GridPoint) obj);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.getIntX();
        hash = 53 * hash + this.getIntY();
        return hash;
    }

}
