package contest_2ima20.client.algorithms.utils;

import contest_2ima20.core.boundaryembedding.Input;

import contest_2ima20.core.boundaryembedding.GridPoint;
import contest_2ima20.core.boundaryembedding.Direction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GridFunctions {
    // Make FindMaxCycleSet function
    public static List<List<Integer>> findMaxCycleSet(List<Direction> P) {
        List<GridPoint> coordinates = new ArrayList<>();
        Set<List<Integer>> cycleSet = new HashSet<>(); // Use a Set to avoid duplicate cycles
        Map<GridPoint, Integer> coordinateToIndexMap = new HashMap<>();

        GridPoint currentCoordinate = new GridPoint(0, 0);
        coordinates.add(currentCoordinate);
        coordinateToIndexMap.put(currentCoordinate, 0);

        for (int i = 0; i < P.size(); i++) {
            GridPoint nextCoordinate = getNextCoordinate(currentCoordinate, P.get(i));
            if (coordinateToIndexMap.containsKey(nextCoordinate)) {
                int cycleStartIndex = coordinateToIndexMap.get(nextCoordinate);
                List<Integer> cycle = IntStream.rangeClosed(cycleStartIndex, i)
                                                .boxed()
                                                .collect(Collectors.toList());
                cycleSet.add(cycle);
            } else {
                coordinates.add(nextCoordinate);
                coordinateToIndexMap.put(nextCoordinate, i + 1);
            }
            currentCoordinate = nextCoordinate;
        }
        return new ArrayList<>(cycleSet); // Convert Set to List
    }

    private static GridPoint getNextCoordinate(GridPoint currentCoordinate, Direction direction) {
        switch (direction) {
            case DOWN:
                return new GridPoint(currentCoordinate.getIntX(), currentCoordinate.getIntY() - 1);
            case UP:
                return new GridPoint(currentCoordinate.getIntX(), currentCoordinate.getIntY() + 1);
            case RIGHT:
                return new GridPoint(currentCoordinate.getIntX() + 1, currentCoordinate.getIntY());
            case LEFT:
                return new GridPoint(currentCoordinate.getIntX() - 1, currentCoordinate.getIntY());
            default:
                return new GridPoint(currentCoordinate.getIntX(), currentCoordinate.getIntY());
        }
    }

    public static boolean findSolutionOfSize(int s, List<Direction> p, List<List<Integer>> cycles, List<Direction> solution) {
        if (cycles.isEmpty() || s == 0) {
            solution.addAll(p); // Assuming solution is initially empty
            return true;
        }
    
        for (List<Integer> cycle : cycles) {
            for (Integer index : cycle) {
                Direction originalDirection = p.get(index);
                for (Direction direction : Direction.values()) {
                    if (direction != originalDirection) {
                        List<Direction> pPrime = new ArrayList<>(p);
                        pPrime.set(index, direction);
                        List<List<Integer>> cycleSetPrime = findMaxCycleSet(pPrime);
                        if (cycleSetPrime.size() <= s - 1) {
                            boolean found = findSolutionOfSize(s - 1, pPrime, cycleSetPrime, solution);
                            if (found) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static List<Direction> smartBruteForce(List<Direction> p) {
        List<List<Integer>> maxCycleSet = findMaxCycleSet(p);
        int lowerBound = maxCycleSet.size();
        
        if (lowerBound == 0) {
            return new ArrayList<>(p); // Return a copy of the original path if no cycles
        }
        
        List<Direction> solution = new ArrayList<>();

        System.out.println("Lower bound: " + lowerBound);
        System.out.println("Path size: " + p.size());

        for (int i = lowerBound; i <= p.size(); i++) {
            System.out.println("Trying size " + i);
            if (findSolutionOfSize(i, new ArrayList<>(p), maxCycleSet, solution)) {
                System.out.println("Found solution of size " + i);
                return solution;
            }
        }
        
        System.out.println("No solution found");
        return p; // Return an empty list if no solution
    }


    // This function translates the entire solution to a starting position such that the solution fits
    public static List<GridPoint> translateSolution(List<GridPoint> solution, Input input) {
        
        // First find the dimensions of the solution
        int minX = 0;
        int minY = 0;
        int maxX = 0;
        int maxY = 0;

        for (GridPoint point : solution) {
            if (point.getIntX() < minX) {
                minX = point.getIntX();
            } else if (point.getIntX() > maxX) {
                maxX = point.getIntX();
            }
            if (point.getIntY() < minY) {
                minY = point.getIntY();
            } else if (point.getIntY() > maxY) {
                maxY = point.getIntY();
            }
        }
        int solutionWidth = maxX - minX;
        int solutionHeight = maxY - minY;

        // If solution dimensions are small enough translate each point accordingly
        if (solutionWidth <= input.width && solutionHeight <= input.height) {
            for (GridPoint point : solution) {
                point.translate(-minX, -minY);
            }
        }

        return solution;
    }
}
