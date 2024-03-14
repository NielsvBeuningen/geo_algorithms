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
        
        // Use smart brute force to find the best solution
        List<Direction> best_solution = GridFunctions.smartBruteForce(input.directions, input.width, input.height);

        // INCLUDE STARTING POSITION MECHANICS

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

}
