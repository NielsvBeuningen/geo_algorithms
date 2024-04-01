package contest_2ima20.client.algorithms.utils;

import contest_2ima20.core.boundaryembedding.Input;

import contest_2ima20.core.boundaryembedding.GridPoint;
import contest_2ima20.core.boundaryembedding.Direction;

import nl.tue.geometrycore.geometry.Vector;

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





    public static CycleSetAndOutOfBounds findMaxCycleSetOOB(List<Direction> P, int grid_width, int grid_height, int start_x, int start_y) {
        List<GridPoint> coordinates = new ArrayList<>();
        Set<List<Integer>> cycleSet = new HashSet<>();
        List<GridPoint> outOfBoundsPoints = new ArrayList<>();

        Map<GridPoint, Integer> coordinateToIndexMap = new HashMap<>();

        GridPoint currentCoordinate = new GridPoint(start_x, start_y);
        coordinates.add(currentCoordinate);
        coordinateToIndexMap.put(currentCoordinate, 0);

        for (int i = 0; i < P.size(); i++) {
            GridPoint nextCoordinate = getNextCoordinate(currentCoordinate, P.get(i));

            // Check if the nextCoordinate is out of bounds
            if (nextCoordinate.getIntX() < 0 || nextCoordinate.getIntX() > grid_width || nextCoordinate.getIntY() < 0 || nextCoordinate.getIntY() > grid_height) {
                outOfBoundsPoints.add(nextCoordinate);
            }

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
        return new CycleSetAndOutOfBounds(new ArrayList<>(cycleSet), outOfBoundsPoints);
    }

    public static boolean findSolutionOfSizeNEW(
        int s, 
        List<Direction> p, 
        CycleSetAndOutOfBounds initialResults, 
        List<Direction> solution, 
        int grid_width, 
        int grid_height,
        int start_x,
        int start_y) {

        List<List<Integer>> cycles = initialResults.getCycles();
        List<GridPoint> initialOutOfBoundsPoints = initialResults.getOutOfBoundsPoints();

        // System.out.println("OOB points: " + initialOutOfBoundsPoints.size() + " Cycles: " + cycles.size());

        if ((cycles.isEmpty() && initialOutOfBoundsPoints.isEmpty()) || s == 0) {
            solution.addAll(p); // Assuming solution is initially empty
            return true;
        }

        for (List<Integer> cycle: cycles) {
            for (Integer index : cycle) {
                Direction originalDirection = p.get(index);
                for (Direction direction : Direction.values()) {
                    if (direction != originalDirection) {
                        List<Direction> pPrime = new ArrayList<>(p);
                        pPrime.set(index, direction);
                        CycleSetAndOutOfBounds resultPrime = findMaxCycleSetOOB(
                            pPrime, 
                            grid_width, grid_height, 
                            start_x, start_y);
                        List<List<Integer>> cycleSetPrime = resultPrime.getCycles();
                        List<GridPoint> outOfBoundsPointsPrime = resultPrime.getOutOfBoundsPoints();

                        // Check if the new direction reduces cycles or OOB points
                        int problems = cycleSetPrime.size() + outOfBoundsPointsPrime.size();

                        if (problems <= s - 1) {
                            boolean found = findSolutionOfSizeNEW(
                                s - 1, 
                                pPrime, resultPrime, 
                                solution, 
                                grid_width, grid_height,
                                start_x, start_y);
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

    public static List<Direction> smartBruteForce(List<Direction> p, int grid_width, int grid_height, int start_x, int start_y) {
        CycleSetAndOutOfBounds maxCycleSetOOB = findMaxCycleSetOOB(
            p, 
            grid_width, grid_height,
            start_x, start_y);
        int lowerBound = maxCycleSetOOB.getCycles().size();
        int OOB_bound = maxCycleSetOOB.getOutOfBoundsPoints().size();

        int problems = lowerBound + OOB_bound;

        if (problems == 0) {
            return new ArrayList<>(p); // Return a copy of the original path if no cycles
        }

        List<Direction> solution = new ArrayList<>();

        System.out.println("Lower bound: " + lowerBound);
        System.out.println("Path size: " + p.size());

        for (int i = problems; i <= p.size(); i++) {
            System.out.println("Trying size " + i);
            if (findSolutionOfSizeNEW(
                i, 
                new ArrayList<>(p), maxCycleSetOOB, 
                solution, 
                grid_width, grid_height,
                start_x, start_y)) {
                System.out.println("Found solution of size " + i);
                return solution;
            }
        }

        System.out.println("No solution found");
        return new ArrayList<Direction>(); // Return an empty list if no solution
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
        // Solution dimensions defined as Vector(Width, Height)
        // int solWidth = maxX - minX;
        // int solHeight = maxY - minY;

        // Input dimensions
        Vector dimensions = new Vector(input.width, input.height);

        // Initialize the minimal penalty for all translations
        Integer minPenalty = 100000;
        Vector bestTranslation = new Vector(0,0);

        if (getTranslationPenalty(solution, dimensions, new Vector(input.width-maxX, input.height-maxY)) <= minPenalty) {
            // Upper right corner
            minPenalty = getTranslationPenalty(solution, dimensions, new Vector(input.width-maxX, input.height-maxY));
            bestTranslation = new Vector(input.width-maxX, input.height-maxY);
            System.out.printf("Best translation is upper right with a penalty of: %d%n", minPenalty);
        }
        if (getTranslationPenalty(solution, dimensions, new Vector(-1*minX, input.height-maxY)) <= minPenalty) {
            // Upper left corner
            minPenalty = getTranslationPenalty(solution, dimensions, new Vector(-1*minX, input.height-maxY));
            bestTranslation = new Vector(-1*minX, input.height-maxY);
            System.out.printf("Best translation is upper left with a penalty of: %d%n", minPenalty);
        } 
        if (getTranslationPenalty(solution, dimensions, new Vector(input.width-maxX, -1*minY)) <= minPenalty) {
            // Lower right corner
            minPenalty = getTranslationPenalty(solution, dimensions, new Vector(input.width-maxX, -1*minY));
            bestTranslation = new Vector(input.width-maxX, -1*minY);
            System.out.printf("Best translation is lower right with a penalty of: %d%n", minPenalty);
        } 
        if (getTranslationPenalty(solution, dimensions, new Vector(-1*minX, -1*minY)) <= minPenalty) {
            // Lower left corner
            minPenalty = getTranslationPenalty(solution, dimensions, new Vector(-1*minX, -1*minY));
            bestTranslation = new Vector(-1*minX, -1*minY);
            System.out.printf("Best translation is lower left with a penalty of: %d%n", minPenalty);
        } 
        
        for (GridPoint p : solution) {
            p.translate(bestTranslation);
        }
        
        return solution;
    }

    // Function to get the penalty of a translation
    public static int getTranslationPenalty(List<GridPoint> solution, Vector dimensions, Vector translation) {
        // Initialize penalty to 0
        int penalty = 0;

        // Per point check if it is out of bounds, if so add to the penalty
        for (GridPoint point : solution) {
            Vector newPoint = Vector.add(point, translation);
            if (newPoint.getX() < 0 || newPoint.getX() > dimensions.getX() || newPoint.getY() < 0 || newPoint.getY() > dimensions.getY()) {
                penalty++;
            }
        }
        return penalty;
    }
}
