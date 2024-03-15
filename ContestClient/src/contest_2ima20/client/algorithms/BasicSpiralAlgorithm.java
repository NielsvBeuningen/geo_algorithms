/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contest_2ima20.client.algorithms;

import contest_2ima20.core.boundaryembedding.Input;
import contest_2ima20.core.boundaryembedding.Output;
import nl.tue.geometrycore.geometry.Vector;
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
public class BasicSpiralAlgorithm extends BoundaryEmbeddingAlgorithm {

    @Override
    public Output doAlgorithm(Input input) {

        Output output = new Output(input);
        GridPoint gp = new GridPoint(0, 0);
        output.embedding.add(gp);
        int length = input.directions.size();
        int startWidth = input.width;
        int startHeight = input.height;
        int width = input.width;
        int height = input.height;
        int direction = 0; // 0: right, 1: down, 2: left, 3: up
        int steps = 1; // Number of steps to take in the current direction
        int stepCount = 0; // Counter to keep track of steps taken
        int x = 0, y = 0;
        for (int i = 0; i < length; i++) {
            stepCount++;
            if (direction == 0 && stepCount >= steps) {
                x++;
                stepCount = 0;
            } else if (direction == 1 && stepCount >= steps) {
                y++;
                stepCount = 0;
            } else if (direction == 2 && stepCount >= steps) {
                x--;
                stepCount = 0;
            } else if (direction == 3 && stepCount >= steps) {
                y--;
                stepCount = 0;
            }

            if (direction == 0 && x == width - 1) {
                direction = 1;
                width--;
            } else if (direction == 1 && y == height - 1) {
                direction = 2;
                height--;
            } else if (direction == 2 && x == startWidth - width) {
                direction = 3;
            } else if (direction == 3 && y == startHeight - height) {
                direction = 0;
            }
            gp = new GridPoint(x, y);
            output.embedding.add(gp);
    }
    return output;
        
}
}
