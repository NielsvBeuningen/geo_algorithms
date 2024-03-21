/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contest_2ima20.client.algorithms;

import contest_2ima20.core.boundaryembedding.Input;
import contest_2ima20.core.boundaryembedding.Output;

import java.util.ArrayList;
import java.util.List;

import contest_2ima20.client.boundaryembedding.BoundaryEmbeddingAlgorithm;
import contest_2ima20.core.boundaryembedding.Direction;
import contest_2ima20.core.boundaryembedding.GridPoint;

// Custom imports
import contest_2ima20.client.algorithms.utils.GridFunctions;

/**
 *
 * @author Niels van Beuningen (n.p.g.t.v.beuningen@student.tue.nl)
 */
public class TurboDirector extends BoundaryEmbeddingAlgorithm {

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
                // Use smart brute force to find the best solution
                List<Direction> best_solution = GridFunctions.smartBruteForce(
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

}
