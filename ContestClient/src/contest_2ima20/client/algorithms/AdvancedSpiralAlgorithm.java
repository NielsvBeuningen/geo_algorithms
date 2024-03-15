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
public class AdvancedSpiralAlgorithm extends BoundaryEmbeddingAlgorithm {

    public int calcMaxLength(int width, int step) {
        int length = width;

        for (int i = width; i > 0; i = i - step) {
            length = length + (2*i); 
        }

        System.out.println(length);

        return length;
    }

    public int calcInwardStep(Input input) {
        int step = 1; 
        int x = input.width;

        for (int i = 1; i < x; i++ ) {
            System.out.println(i);
            if (input.directions.size() >= calcMaxLength(x, i)) {
                step = i-1;
                break;
            }
        }
        return step;
    }

    @Override
    public Output doAlgorithm(Input input) {

        Output output = new Output(input);
        GridPoint gp = new GridPoint(0, 0);
        output.embedding.add(gp); // start point

        int length = input.directions.size(); // the amount of arrows
        int startWidth = input.width; // the grid width
        int startHeight = input.height; // the grid height
        int width = input.width; // the width of the current spiral
        int height = input.height; // the height of the current spiral

        int direction = 0; // 0: right, 1: down, 2: left, 3: up
        int x = 0, y = 0;
        int inwardStep = calcInwardStep(input) ; // the step to the centre

        for (int i = 0; i < length; i++) {

            // change coordinates in the dircetion
            if (direction == 0) {
                x++;
            } else if (direction == 1) {
                y++;
            } else if (direction == 2) {
                x--;
            } else if (direction == 3) {
                y--;
            }

            // check if the arrow hits the outer bound of the spiral
            if (direction == 0 && x == width) {
                direction = 1;
                width = width - inwardStep;
            } else if (direction == 1 && y == height) {
                direction = 2;
                height = height - inwardStep;
            } else if (direction == 2 && x == startWidth - width - inwardStep) {
                direction = 3;
            } else if (direction == 3 && y == startHeight - height) {
                direction = 0;
            }

            // add the grid point
            gp = new GridPoint(x, y);
            output.embedding.add(gp);
    }
    return output;
    }
    
        
}

