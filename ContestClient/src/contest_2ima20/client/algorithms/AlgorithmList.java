/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contest_2ima20.client.algorithms;

import contest_2ima20.client.boundaryembedding.BoundaryEmbeddingAlgorithm;

/**
 *
 * @author Wouter Meulemans (w.meulemans@tue.nl)
 */
public class AlgorithmList {

    public static BoundaryEmbeddingAlgorithm[] getAlgorithms() {
        return new BoundaryEmbeddingAlgorithm[]{
            new ArbitraryAlgorithm(),
            new BasicAlgorithm(),
            new OptimusPriem(),
            new KoenAlgorithm()
        };
    }
}
