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
import contest_2ima20.client.algorithms.utils.GridFunctions;
// Custom import
import contest_2ima20.client.algorithms.utils.Problems;

/**
 *
 * @author Maarten van Sluijs (m.d.v.sluijs@student.tue.nl)
 */
public class Info extends BoundaryEmbeddingAlgorithm {

    @Override
    public Output doAlgorithm(Input input) {

        Output output = new Output(input);

        System.out.printf("Problem has a width: %d, and height: %d%n", input.width, input.height);
        System.out.printf("Ratio is %f%n", (float) input.directions.size()/Math.min(input.width, input.height));

        System.out.printf("Number of cycles is: %d%n", findMaxCycleSet(input.directions).size());

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
}
