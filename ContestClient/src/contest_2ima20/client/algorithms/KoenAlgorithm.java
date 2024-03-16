/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contest_2ima20.client.algorithms;

import contest_2ima20.core.boundaryembedding.Input;
import contest_2ima20.core.boundaryembedding.Output;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import contest_2ima20.client.boundaryembedding.BoundaryEmbeddingAlgorithm;
import contest_2ima20.core.boundaryembedding.Direction;
import contest_2ima20.core.boundaryembedding.GridPoint;
import nl.tue.geometrycore.geometry.Vector;

/**
 *
 * @author Koen Kaandorp k.d.kaandorp@student.tue.nl
 */
public class KoenAlgorithm extends BoundaryEmbeddingAlgorithm {

    @Override
    public Output doAlgorithm(Input input) {


        int down = 0;
        int up = 0;
        int left = 0;
        int right = 0;
        List<Integer> xCoord = new ArrayList<>();
        List<Integer> yCoord = new ArrayList<>();
        Output output = new Output(input);
        xCoord.add(0);
        yCoord.add(0);
        int xCoordInt = 0; // HEEEEEL Lelijk
        int yCoordInt = 0;

        // find the direction counts and find the min and max width and height of the sequence
        for (Direction d : input.directions){
            if (d == Direction.DOWN) {
                down = down + 1;
                yCoordInt = yCoordInt - 1;
                yCoord.add(yCoordInt);
            } else if (d == Direction.UP) {
                up = up +1;
                yCoordInt = yCoordInt + 1;
                yCoord.add(yCoordInt);
            } else if (d == Direction.LEFT) {
                left = left + 1;
                xCoordInt = xCoordInt - 1;
                xCoord.add(xCoordInt);
            } else if (d == Direction.RIGHT) {
                right = right +1;
                xCoordInt = xCoordInt + 1;
                xCoord.add(xCoordInt);
            }
        }
        
        int xMax = Collections.max(xCoord);
        int xMin = Collections.min(xCoord);
        int yMax = Collections.max(yCoord);
        int yMin = Collections.min(yCoord);

        int xStart = (input.width/2)+left-right;
        int yStart = (input.height/2)+down-up;
        System.out.println(yMax);
        System.out.println(yMin);
        System.out.println(xMax);
        System.out.println(xMin);
        Boolean breakOut = false;

        // find starting position where the entire sequence fits the grid
        for (int i = 0; i < input.width; i++ ) {
            for (int j = 0; j < input.height; j++) {
                if (i+xMin>-1 && i+xMax<input.width && j+yMin>-1 && j+yMax<input.height) {
                    xStart = i;
                    yStart = j;
                    breakOut = true;
                    break;
                }
            }
            if(breakOut) {
                break;
            }
        }
        // Start point
        GridPoint gp = new GridPoint(xStart,yStart);
        output.embedding.add(gp);

        // go throught sequence
        for (Direction d : input.directions) {
            gp = new GridPoint(gp);
            if (d == Direction.DOWN) {
                gp.translate(Vector.down());
            } else if (d == Direction.RIGHT) {
                gp.translate(Vector.right());
            } else if (d == Direction.LEFT) {
                gp.translate(Vector.left());
            } else if (d == Direction.UP) {
                gp.translate(Vector.up());
            }
            
            output.embedding.add(gp);
        }        
        
        // return the result (it's going to be marvelous)
        return output;
    }

}
