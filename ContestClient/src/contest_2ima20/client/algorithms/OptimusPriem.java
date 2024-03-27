/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contest_2ima20.client.algorithms;

import contest_2ima20.core.boundaryembedding.Input;
import contest_2ima20.core.boundaryembedding.Output;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import contest_2ima20.client.boundaryembedding.BoundaryEmbeddingAlgorithm;
import contest_2ima20.core.boundaryembedding.Direction;
import contest_2ima20.core.boundaryembedding.GridPoint;

// Custom import
import contest_2ima20.client.algorithms.utils.CycleSetAndOutOfBounds;

/**
 *
 * @author Niels van Beuningen (n.p.g.t.v.beuningen@student.tue.nl)
 */
public class OptimusPriem extends BoundaryEmbeddingAlgorithm {

    @Override
    public Output doAlgorithm(Input input) {
        // creating an output
        Output output = new Output(input);
        int start_x = 0;
        int start_y = 0;
        GridPoint start_gp = new GridPoint(start_x, start_y);
        List<Direction> final_solution = new ArrayList<>();

        // Loop over possible starting positions and find solution
        for (int x = 0; x < input.width; x++) {
            for (int y = 0; y < input.height; y++) {
                System.out.println("Trying starting position: " + x + ", " + y);
                
                List<Direction> best_solution = findSolution(
                    input.directions, 
                    input.width, 
                    input.height,
                    x, y);

                // If length of solution is larger than 0 break
                if (best_solution.size() > 0) {
                    start_x = x;
                    start_y = y;
                    start_gp = new GridPoint(start_x, start_y);
                    final_solution = best_solution;
                    break;
                }
            }
            if (final_solution.size() > 0) {
                break;
            }
        }

        // Convert the best solution to a list of GridPoints
        List<GridPoint> best_solution_gp = new ArrayList<>();
        best_solution_gp.add(start_gp);
        for (Direction d : final_solution) {
            GridPoint next_gp = new GridPoint(best_solution_gp.get(best_solution_gp.size() - 1));
            next_gp.translate(d.toVector());
            best_solution_gp.add(next_gp);
        }

        // Add the best solution to the output
        for (GridPoint gp : best_solution_gp) {
            output.embedding.add(gp);
        }

        return output;
    }

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



    public static CycleSetAndOutOfBounds getCyclesOOB(
        List<Direction> P, 
        int grid_width, int grid_height, 
        int start_x, int start_y) {

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

    private static Map<String, Boolean> memo = new HashMap<>();

    private static String createMemoKey(int s, List<Direction> p, int grid_width, int grid_height, int start_x, int start_y) {
        // Create a unique key based on parameters. This is a simplified version.
        // Consider including more specifics to avoid collisions.
        return s + "-" + p.toString() + "-" + grid_width + "-" + grid_height + "-" + start_x + "-" + start_y;
    }

    public static boolean findSolution(
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

        String key = createMemoKey(s, p, grid_width, grid_height, start_x, start_y);
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

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
                        CycleSetAndOutOfBounds resultPrime = getCyclesOOB(
                            pPrime, 
                            grid_width, grid_height, 
                            start_x, start_y);
                        List<List<Integer>> cycleSetPrime = resultPrime.getCycles();
                        List<GridPoint> outOfBoundsPointsPrime = resultPrime.getOutOfBoundsPoints();
    
                        // Check if the new direction reduces cycles or OOB points
                        int problems = cycleSetPrime.size() + outOfBoundsPointsPrime.size();
                        
                        if (problems <= s - 1) {
                            boolean found = findSolution(
                                s - 1, 
                                pPrime, resultPrime, 
                                solution, 
                                grid_width, grid_height,
                                start_x, start_y);
                            if (found) {
                                memo.put(key, found);
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public static List<Direction> findSolution(List<Direction> p, int grid_width, int grid_height, int start_x, int start_y) {
        CycleSetAndOutOfBounds maxCycleSetOOB = getCyclesOOB(
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
            if (findSolution(
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

}
