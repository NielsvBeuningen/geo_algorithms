package contest_2ima20.client.algorithms.utils;

import contest_2ima20.core.boundaryembedding.GridPoint;
import contest_2ima20.core.boundaryembedding.Direction;

import java.util.List;

// public class CycleSetAndOutOfBounds {
//     List<List<Integer>> cycles;
//     List<GridPoint> outOfBoundsPoints;

//     public CycleSetAndOutOfBounds(List<List<Integer>> cycles, List<GridPoint> outOfBoundsPoints) {
//         this.cycles = cycles;
//         this.outOfBoundsPoints = outOfBoundsPoints;
//     }

//     public List<List<Integer>> getCycles() {
//         return cycles;
//     }

//     public List<GridPoint> getOutOfBoundsPoints() {
//         return outOfBoundsPoints;
//     }
// }

public class Problems {
    List<List<Integer>> problems;

    // Class should contain list of problems, either being cycles or out of bounds points
    // This class is used to store the problems that are found in the boundary embedding algorithm
    // Each problem contains a list of integers, which represent the indices of the points in the problem
    // It also contains the class of the problem, which is either a cycle or an out of bounds point
    public Problems(List<List<Integer>> problems) {
        this.problems = problems;
    }

    public List<List<Integer>> getProblems() {
        return problems;
    }
}
