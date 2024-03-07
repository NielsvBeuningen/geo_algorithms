package contest_2ima20.client.algorithms.utils;

import contest_2ima20.core.boundaryembedding.GridPoint;
import contest_2ima20.core.boundaryembedding.Direction;
import contest_2ima20.core.boundaryembedding.Input;
import nl.tue.geometrycore.geometry.Vector;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GridFunctions {
    // Make FindMaxCycleSet function
    public static List<List<Integer>> FindMaxCycleSet(List<Direction> P) {
        List<List<Integer>> CycleSet = new ArrayList<>();
        List<GridPoint> Coordinates = new ArrayList<>();
        Coordinates.add(new GridPoint(0, 0));
        int EndPreviousCycle = 0;
        GridPoint CurrentCoordinate = new GridPoint(0, 0);
        for (int i = 0; i < P.size(); i++) {
            GridPoint NextCoordinate = new GridPoint(0, 0);
            if (P.get(i).toVector() == Vector.down()) {
                NextCoordinate = new GridPoint(CurrentCoordinate.getIntX(), CurrentCoordinate.getIntY() - 1);
            } else if (P.get(i).toVector() == Vector.up()) {
                NextCoordinate = new GridPoint(CurrentCoordinate.getIntX(), CurrentCoordinate.getIntY() + 1);
            } else if (P.get(i).toVector() == Vector.right()) {
                NextCoordinate = new GridPoint(CurrentCoordinate.getIntX() + 1, CurrentCoordinate.getIntY());
            } else if (P.get(i).toVector() == Vector.left()) {
                NextCoordinate = new GridPoint(CurrentCoordinate.getIntX() - 1, CurrentCoordinate.getIntY());
            }
            if (!Coordinates.contains(NextCoordinate)) {
                Coordinates.add(NextCoordinate);
            } else {
                List<Integer> cycle = new ArrayList<>();
                cycle.add(Coordinates.indexOf(NextCoordinate));
                cycle.add(i + 1);
                CycleSet.add(cycle);
                EndPreviousCycle = i + 1;
                Coordinates.clear();
                Coordinates.add(NextCoordinate);
            }
            CurrentCoordinate = NextCoordinate;
        }
        return CycleSet;
    }

    public static boolean FindSolutionOfSize(int s, List<Direction> p, List<Direction> originalP, List<List<Integer>> C, List<Direction> Solution) {
        List<Integer> Cycle = C.get(0);
        for (int i = 0; i < Cycle.size(); i++) {
            Vector OriginalDirection = originalP.get(Cycle.get(i)).toVector();
            if (p.get(i).toVector() == OriginalDirection) {
                List<Direction> AllDirections = new ArrayList<Direction>(){{
                    add(Direction.DOWN); 
                    add(Direction.UP); 
                    add(Direction.RIGHT); 
                    add(Direction.LEFT);
                }};

                // Add all directions except the original direction
                for (Direction direction : AllDirections) {
                    if (direction.toVector() != OriginalDirection) {
                        List<Direction> p_prime = new ArrayList<>(p);
                        
                        p.set(i, direction);
                        p_prime = p;
                        List<List<Integer>> CycleSet = FindMaxCycleSet(p_prime);

                        if (CycleSet.size() == 0) {
                            Solution = p_prime;
                            return true;
                        } else if (CycleSet.size() <= s - 1) {
                            if (FindSolutionOfSize(s - 1, p_prime, originalP, CycleSet, Solution)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    // Make SmartBruteForce function
    public static List<Direction> SmartBruteForce(List<Direction> P) {
        List<List<Integer>> MaxCycleSet = FindMaxCycleSet(P);
        int LowerBound = MaxCycleSet.size();
        if (LowerBound == 0) {
            return P;
        }
        boolean FoundSolution = false;
        List<Direction> Solution = new ArrayList<>();

        for (int i = LowerBound; i < P.size(); i++) {
            FoundSolution = FindSolutionOfSize(i, P, P, MaxCycleSet, Solution);
            if (FoundSolution) {
                System.out.println("Found solution");
                System.out.println("Found solution of size " + i);
                return Solution;
            }
        }
        System.out.println("No solution found");
        return Solution;
    }
}
