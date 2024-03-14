package contest_2ima20.client.algorithms.utils;

import contest_2ima20.core.boundaryembedding.GridPoint;
import contest_2ima20.core.boundaryembedding.Direction;

import java.util.List;

public class CycleSetAndOutOfBounds {
    List<List<Integer>> cycles;
    List<GridPoint> outOfBoundsPoints;

    public CycleSetAndOutOfBounds(List<List<Integer>> cycles, List<GridPoint> outOfBoundsPoints) {
        this.cycles = cycles;
        this.outOfBoundsPoints = outOfBoundsPoints;
    }

    public List<List<Integer>> getCycles() {
        return cycles;
    }

    public List<GridPoint> getOutOfBoundsPoints() {
        return outOfBoundsPoints;
    }
}
