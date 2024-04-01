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
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Koen Kaandorp k.d.kaandorp@student.tue.nl
 */
public class AdvancedSpiralAlgorithm extends BoundaryEmbeddingAlgorithm {

    public int calcMaxLength(int width, int step) {
        int length = width;

        for (int i = width; i > 0; i = i - step) {
            length = length + (2*i); 
        }

        return length;
    }

    public int calcInwardStep(Input input) {
        int step = 1; 
        int x = input.width;

        for (int i = 1; i < x; i++ ) {
            if (input.directions.size() >= calcMaxLength(x, i)) {
                step = i-1;
                break;
            }
        }
        return step;
    }

    public List<Integer> inwardArrowsIndex(Input input, int length, int i, Direction dirIn, Direction dirOut, int inwardStep) {
        List<Integer> indexList = new ArrayList<Integer>();
        List<Integer> indexListIn = new ArrayList<Integer>();
        List<Integer> indexListOut = new ArrayList<Integer>();
        int dirInCount = 0;
        int dirOutCount = 0;
        int inwardAmount = 0;
        
        for (int l = i; l < length; l++) {
            if(l>input.directions.size()-1) {
                break;
            }
            if (input.directions.get(l) == dirIn) {
                dirInCount++;
            } else if (input.directions.get(l) == dirOut && dirOutCount < dirInCount) {
                length = length + 2;
                dirOutCount++;
            }
        }
        
        dirInCount = dirOutCount;

        for (int l = i; l < length; l++) {
            if (l>input.directions.size()-1) {
                break;
            }
            if (input.directions.get(l) == dirIn && inwardAmount <= inwardStep-1 && dirInCount != 0) {
                inwardAmount++;
                length++;
                dirInCount--;
                if(indexListOut.size() == 0 || indexListOut.get(indexListOut.size()-1) != l-1) {
                    indexListIn.add(l);
                }
            } else if (input.directions.get(l) == dirOut && dirOutCount > dirInCount) {
                inwardAmount--;
                length++;
                dirOutCount--;
                if(indexListIn.size() == 0 || indexListIn.get(indexListIn.size()-1) != l-1) {
                    indexListOut.add(l);
                }
            }
        }
        indexList.addAll(indexListOut);
        indexList.addAll(indexListIn);
        Collections.sort(indexList);
        return indexList;
    }

    public Output pathDirectionRight(Input input, int x, int y, int inwardStep, int length, int startDirection, int width, int height) {
        int direction = startDirection;
        int startHeight = height;
        int startWidth = width;

        Output output = new Output(input);

        GridPoint gp = new GridPoint(x, y);
        output.embedding.add(gp); // start point

        List<Integer> indexList = inwardArrowsIndex(input, width-2, 0, Direction.UP, Direction.DOWN, inwardStep);
        
        for (int i = 0; i < length; i++) {
            // change coordinates in the dircetion
            if (direction == 0) {
                if (indexList.size() > 0 && i == indexList.get(0)) {
                    if (input.directions.get(i) == Direction.UP) {
                        y++;
                    } else {
                        y--;
                    }
                    indexList.remove(0);
                } else {
                    x++;
                }
            } else if (direction == 1) {
                if (indexList.size() > 0 && i == indexList.get(0)) {
                    if (input.directions.get(i) == Direction.LEFT) {
                        x--;
                    } else {
                        x++;
                    }
                    indexList.remove(0);
                } else {
                    y++;
                }
            } else if (direction == 2) {
                if (indexList.size() > 0 && i == indexList.get(0)) {
                    if (input.directions.get(i) == Direction.DOWN) {
                        y--;
                    } else {
                        y++;
                    }
                    indexList.remove(0);
                } else {
                    x--;
                }
            } else if (direction == 3) {
                if (indexList.size() > 0 && i == indexList.get(0)) {
                    if (input.directions.get(i) == Direction.RIGHT) {
                        x++;
                    } else {
                        x--;
                    }
                    indexList.remove(0);
                } else {
                    y--;
                };
            }

            // check if the arrow hits the outer bound of the spiral
            if (direction == 0 && x == width) {
                direction = 1;
                width = width - inwardStep;
                indexList.clear();
                indexList = inwardArrowsIndex(input, width+i-2, i+2, Direction.LEFT, Direction.RIGHT, inwardStep);
            } else if (direction == 1 && y == height) {
                direction = 2;
                height = height - inwardStep;
                indexList.clear();
                indexList = inwardArrowsIndex(input, height+i-2, i+2, Direction.DOWN, Direction.UP, inwardStep);
            } else if (direction == 2 && x == startWidth - width - inwardStep) {
                direction = 3;
                indexList.clear();
                indexList = inwardArrowsIndex(input, width+i-2, i+2, Direction.RIGHT, Direction.LEFT, inwardStep);
            } else if (direction == 3 && y == startHeight - height) {
                direction = 0;
                indexList.clear();
                indexList = inwardArrowsIndex(input, height+i-2, i+2, Direction.UP, Direction.DOWN, inwardStep);
            }

            // add the grid point
            gp = new GridPoint(x, y);
            output.embedding.add(gp);
    }
        return output;
    }

    public Output pathDirectionLeft(Input input, int x, int y, int inwardStep, int length, int startDirection, int width, int height) {
        int direction = startDirection;
        int startHeight = height;
        int startWidth = width;

        Output output = new Output(input);

        GridPoint gp = new GridPoint(x, y);
        output.embedding.add(gp); // start point
        
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
            if (direction == 0 && x == width-inwardStep) {
                direction = 1;
            } else if (direction == 1 && y == height) {
                direction = 2;
            } else if (direction == 2 && x == startWidth - width) {
                direction = 3;
                width = width - inwardStep;
            } else if (direction == 3 && y == startHeight - height) {
                direction = 0;
                height = height - inwardStep;
            }

            // add the grid point
            gp = new GridPoint(x, y);
            output.embedding.add(gp);
    }
        return output;
    }

    public Output pathDirectionDown(Input input, int x, int y, int inwardStep, int length, int startDirection, int width, int height) {
        int direction = startDirection;
        int startHeight = height;
        int startWidth = width;

        Output output = new Output(input);

        GridPoint gp = new GridPoint(x, y);
        output.embedding.add(gp); // start point
        
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
            } else if (direction == 1 && y == height-inwardStep) {
                direction = 2;
            } else if (direction == 2 && x == startWidth - width) {
                direction = 3;
            } else if (direction == 3 && y == startHeight - height) {
                direction = 0;
                height = height - inwardStep;
            }

            // add the grid point
            gp = new GridPoint(x, y);
            output.embedding.add(gp);
    }
        return output;
    }

    public Output pathDirectionUp(Input input, int x, int y, int inwardStep, int length, int startDirection, int width, int height) {
        int direction = startDirection;
        int startHeight = height;
        int startWidth = width;

        Output output = new Output(input);

        GridPoint gp = new GridPoint(x, y);
        output.embedding.add(gp); // start point
        
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
            } else if (direction == 1 && y == height) {
                direction = 2;
                height = height - inwardStep;
            } else if (direction == 2 && x == startWidth - width) {
                direction = 3;
                width = width - inwardStep;
            } else if (direction == 3 && y == startHeight - height -inwardStep) {
                direction = 0;
            }

            // add the grid point
            gp = new GridPoint(x, y);
            output.embedding.add(gp);
    }
        return output;
    }

    public Output determinePath(Input input, int xStart, int yStart, int inwardStep) {

        Output output = new Output(input);
        int length = input.directions.size(); // the amount of arrows
        int width = input.width; // the width of the current spiral
        int height = input.height; // the height of the current spiral

        int direction = 4; // 0: right, 1: up, 2: left, 3: down, 4: nothing
        int x = xStart, y = yStart;

        // calc start direction
        if (y == width && x != 0) {
            direction = 2;
            output = pathDirectionLeft(input, x, y, inwardStep, length, direction, width, height);
        } else if (x == width && y != width) {
            direction = 1;
            output = pathDirectionUp(input, x, y, inwardStep, length, direction, width, height);
        } else if (y == 0 && x != width) {
            direction = 0;
            output = pathDirectionRight(input, x, y, inwardStep, length, direction, width, height);
        } else if (x == 0 && y != 0) {
            direction = 3;
            output = pathDirectionDown(input, x, y, inwardStep, length, direction, width, height);
        }

    return output;
    }
    
    public Output createPath(Input input, int inwardStep) {
        Output output = new Output(input);
        int width = input.width;
        double bestPenalty = 99999999999999.99;
        Output bestOutput = new Output(input);
        
        for (int x = 0; x < width+1; x++) {
            for (int y = 0; y < width+1; y++) {
                if (x == 0 || x == width || y == 0 || y == width) {
                    output = determinePath(input, x, y, inwardStep);
                    if (output.computeQuality() < bestPenalty && output.isValid()) {
                        bestPenalty = output.computeQuality();
                        bestOutput = output;
                    }
                }
            }
        }
        //bestOutput = determinePath(input,0, 0, inwardStep);
        return bestOutput;
    }

    @Override
    public Output doAlgorithm(Input input) {
        int inwardStep = calcInwardStep(input);
        
    return createPath(input, inwardStep);
    }
    
        
}

