/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contest_2ima20.core.boundaryembedding;

import contest_2ima20.core.problem.ProblemDefinition;

/**
 *
 * @author Wouter Meulemans (w.meulemans@tue.nl)
 */
public class BoundaryEmbedding extends ProblemDefinition<Input,Output> {

    @Override
    public Input createInputInstance() {
        return new Input();
    }

    @Override
    public Output createOutputFor(Input input) {
        return new Output(input);
    }
    
}
