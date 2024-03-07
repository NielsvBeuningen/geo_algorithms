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
public class BasicAlgorithm extends BoundaryEmbeddingAlgorithm {
    private boolean isValidPlacement(List<GridPoint> temp_output, GridPoint gp, int W, int H) {
        // Check grid bounds
        if (gp.getX() < 0 || gp.getX() > W || gp.getY() < 0 || gp.getY() > H) return false;
        // Check uniqueness constraint
        for (GridPoint placedGp : temp_output) {
            if (gp.equals(placedGp)) return false;
        }
        return true;
    }
    
    private Direction getNextDirection(Direction currentDirection) {
        switch (currentDirection) {
            case UP: return Direction.RIGHT;
            case RIGHT: return Direction.DOWN;
            case DOWN: return Direction.LEFT;
            case LEFT: return Direction.UP;
            default: return Direction.RIGHT; // or handle error
        }
    }

    @Override
    public Output doAlgorithm(Input input) {
        // creating an output
        Output output = new Output(input);
        int W = input.width - 1;
        int H = input.height - 1;

        // Start with (0,0)
        GridPoint gp = new GridPoint(input.width/2, input.height/2);
        output.embedding.add(gp);
        Direction currentDirection = Direction.RIGHT;
        
        // Make temporary list to store the directions
        List<GridPoint> gp_temp = new ArrayList<>();

        for (Direction d : input.directions) {
            // Try the current direction first
            GridPoint nextGp = new GridPoint(gp);
            nextGp.translate(d.toVector());
            
            if (!isValidPlacement(gp_temp, nextGp, W, H)) {
                // If the placement isn't valid, try the next direction
                currentDirection = getNextDirection(currentDirection);
                nextGp = new GridPoint(gp);
                nextGp.translate(currentDirection.toVector());
                
                // If the new direction is still not valid, record a deviation and continue
                if (!isValidPlacement(gp_temp, nextGp, W, H)) {
                    continue; // Skip this placement
                }
            }
            
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
