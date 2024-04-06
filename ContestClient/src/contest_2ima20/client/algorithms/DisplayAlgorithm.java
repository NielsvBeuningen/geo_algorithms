/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contest_2ima20.client.algorithms;

import contest_2ima20.core.boundaryembedding.Input;
import contest_2ima20.core.boundaryembedding.Output;
import contest_2ima20.client.boundaryembedding.BoundaryEmbeddingAlgorithm;
import contest_2ima20.core.boundaryembedding.Direction;
import contest_2ima20.core.boundaryembedding.GridPoint;

// Custom imports
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Niels van Beuningen (n.p.g.t.v.beuningen@student.tue.nl)
 */
public class DisplayAlgorithm extends BoundaryEmbeddingAlgorithm {

    @Override
    public Output doAlgorithm(Input input) {
        int START_X = 0;
        int START_Y = 0;

        // creating an output
        Output output = new Output(input);

        // Start with (0,0)
        GridPoint gp = new GridPoint(START_X, START_Y);
        output.embedding.add(gp);
        
        // Make temporary list to store the directions
        List<GridPoint> gp_temp = new ArrayList<>();

        for (Direction d : input.directions) {
            // Try the current direction first
            GridPoint nextGp = new GridPoint(gp);
            nextGp.translate(d.toVector());
            
            // If a valid placement is found, add to the embedding and update the current position
            gp_temp.add(nextGp);
            gp = nextGp; // Update current GridPoint
        }
        
        for (GridPoint g : gp_temp) {
            output.embedding.add(g);
        }
        
        // return the result (hopefully it's better now)
        return output;
    }
}
