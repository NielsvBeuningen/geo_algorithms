/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contest_2ima20.client.algorithms;

import contest_2ima20.core.boundaryembedding.Input;
import contest_2ima20.core.boundaryembedding.Output;

import java.util.ArrayList;
import java.util.Arrays;
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
import contest_2ima20.client.algorithms.utils.Problems;

/**
 *
 * @author Niels van Beuningen (n.p.g.t.v.beuningen@student.tue.nl)
 */
public class BumbleBeter extends BoundaryEmbeddingAlgorithm {

    @Override
    public Output doAlgorithm(Input input) {
        // creating an output
        Output output = new Output(input);
        int start_x = 0;
        int start_y = 0;
        GridPoint start_gp = new GridPoint(start_x, start_y);


        List<Direction> best_solution = findSolution(
            input.directions, 
            input.width, 
            input.height,
            start_x, start_y);

        

        // Convert the best solution to a list of GridPoints
        List<GridPoint> best_solution_gp = new ArrayList<>();
        best_solution_gp.add(start_gp);
        for (Direction d : best_solution) {
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



    public static Problems getCyclesOOB(
        List<Direction> P, 
        int grid_width, int grid_height, 
        int start_x, int start_y) {

            List<GridPoint> coordinates = new ArrayList<>();
            // Set<List<Integer>> cycleSet = new HashSet<>();
            // Set<List<Integer>> outOfBoundsPoints = new HashSet<>();
        
            Set<List<Integer>> problems = new HashSet<>();

            Map<GridPoint, Integer> coordinateToIndexMap = new HashMap<>();
        
            GridPoint currentCoordinate = new GridPoint(start_x, start_y);
            coordinates.add(currentCoordinate);
            coordinateToIndexMap.put(currentCoordinate, 0);
        

            // OOB variables
            int total_width = 0;
            int total_height = 0;

            int width_counter = 0;
            int height_counter = 0;

            int min_X = 0;
            int max_X = 0;
            int min_Y = 0;
            int max_Y = 0;

            for (int i = 0; i < P.size(); i++) {
                GridPoint nextCoordinate = getNextCoordinate(currentCoordinate, P.get(i));
        
                // Check whether next coordinate increases the width or height, if so, add to out of bounds points
                if (nextCoordinate.getIntX() > max_X) {
                    max_X = nextCoordinate.getIntX();
                    width_counter++;
                } else if (nextCoordinate.getIntX() < min_X) {
                    min_X = nextCoordinate.getIntX();
                    width_counter++;
                } 

                if (nextCoordinate.getIntY() > max_Y){
                    max_Y = nextCoordinate.getIntY();
                    height_counter++;
                } else if (nextCoordinate.getIntY() < min_Y) {
                    min_Y = nextCoordinate.getIntY();
                    height_counter++;
                }

                if (width_counter > total_width) {
                    total_width = width_counter;
                    List<Integer> indices = IntStream.rangeClosed(i, i).boxed().collect(Collectors.toList());
                    problems.add(indices);
                }
                if (height_counter > total_height) {
                    total_height = height_counter;
                    List<Integer> indices = IntStream.rangeClosed(i, i).boxed().collect(Collectors.toList());
                    problems.add(indices);
                }
        
                if (coordinateToIndexMap.containsKey(nextCoordinate)) {
                    int cycleStartIndex = coordinateToIndexMap.get(nextCoordinate);
                    List<Integer> cycle = IntStream.rangeClosed(cycleStartIndex, i)
                                                    .boxed()
                                                    .collect(Collectors.toList());
                    problems.add(cycle);
                } else {
                    coordinates.add(nextCoordinate);
                    coordinateToIndexMap.put(nextCoordinate, i + 1);
                }
                currentCoordinate = nextCoordinate;
            }

            return new Problems(new ArrayList<>(problems));
    }

    public static boolean getSolutionSize(
        int s, 
        List<Direction> p, 
        Problems initialResults, 
        List<Direction> solution, 
        int grid_width, 
        int grid_height,
        int start_x,
        int start_y) {
        
        List<List<Integer>> init_problems = initialResults.getProblems();

        if (init_problems.isEmpty() || s == 0) {
            solution.addAll(p); // Assuming solution is initially empty
            return true;
        }
        
        for (List<Integer> problem: init_problems) {
            for (Integer index: problem) {
                Direction originalDirection = p.get(index);
                for (Direction direction : Direction.values()) {
                    if (direction != originalDirection) {
                        List<Direction> pPrime = new ArrayList<>(p);
                        pPrime.set(index, direction);
                        Problems resultPrime = getCyclesOOB(
                            pPrime, 
                            grid_width, grid_height, 
                            start_x, start_y);
                        List<List<Integer>> problems = resultPrime.getProblems();
                           
                        System.out.println("Problems size: " + problems.size());

                        if (problems.size() <= s - 1) {
                            boolean found = getSolutionSize(
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

    public static List<Direction> findSolution(List<Direction> p, int grid_width, int grid_height, int start_x, int start_y) {
        Problems maxCycleSetOOB = getCyclesOOB(
            p, 
            grid_width, grid_height,
            start_x, start_y);
        int lowerBound = maxCycleSetOOB.getProblems().size();

        if (lowerBound == 0) {
            return new ArrayList<>(p); // Return a copy of the original path if no problems
        }
        
        List<Direction> solution = new ArrayList<>();

        System.out.println("Lower bound: " + lowerBound);
        System.out.println("Path size: " + p.size());

        for (int i = lowerBound; i <= p.size(); i++) {
            System.out.println("Trying size " + i);
            if (getSolutionSize(
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
