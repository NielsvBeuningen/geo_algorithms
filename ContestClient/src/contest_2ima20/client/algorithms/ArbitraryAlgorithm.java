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
import nl.tue.geometrycore.geometry.Vector;

/**
 *
 * @author Wouter Meulemans (w.meulemans@tue.nl)
 */
public class ArbitraryAlgorithm extends BoundaryEmbeddingAlgorithm {

    @Override
    public Output doAlgorithm(Input input) {

        // creating an output
        Output output = new Output(input);

        // let's just go right, surely that's a good idea
        GridPoint gp = new GridPoint(0,1);
        output.embedding.add(gp);
        for (Direction d : input.directions) {
            gp = new GridPoint(gp);
            gp.translate(Vector.right());
            output.embedding.add(gp);
        }        
        
        // return the result (it's going to be marvelous)
        return output;
    }

}
