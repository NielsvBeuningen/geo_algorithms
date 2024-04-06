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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public int maxDeviation(int inwardStep, Input input) {
        int length = calcMaxLength(input.width, inwardStep);
        return length - input.directions.size();
    }

    public Map<String, List<Integer>> inwardArrowsIndex(Input input, int length, int i, Direction dirIn, Direction dirOut, int inwardStep) {
        List<Integer> indexListIn = new ArrayList<>();
        List<Integer> indexListOut = new ArrayList<>();
    
        int inwardAmount = 0;
        int x = 0;
        int y = 0;
        int originalLength = length;
    
        for (int l = i; l < length; l++) {
            if (l == input.directions.size()) {
                break;
            }
    
            if (input.directions.get(l) == dirIn && (inwardAmount != inwardStep -1) &&
                (indexListOut.isEmpty() || indexListOut.get(indexListOut.size() - 1) != l - 1) &&
                (x > (y + 1)) && (originalLength - x) > (y + 2)) {
                inwardAmount++;
                y++;
                length++;
                indexListIn.add(l);
            } else if ((input.directions.get(l) == dirOut && inwardAmount != 0 && 
                       (indexListIn.isEmpty() || indexListIn.get(indexListIn.size() - 1) != l - 1))
                       || ((originalLength - x - 1 == y) && inwardAmount != 0)) {
                inwardAmount--;
                y--;
                length++;
                indexListOut.add(l);
            } else {
                x++;
            }
            //System.out.println((originalLength - x  1) == y);
            
        }
        //System.out.println(indexListIn);
        //System.out.println(indexListOut);
        if (inwardAmount > 0) {
            indexListIn = indexListIn.subList(0, indexListOut.size());
        }
    
        Map<String, List<Integer>> result = new HashMap<>();
        result.put("Index List In", indexListIn);
        result.put("Index List Out", indexListOut);
        return result;
    }

    public Output pathDirectionRight(Input input, int x, int y, int inwardStep, int length, int startDirection, int width, int height) {
        int direction = startDirection;
        int startHeight = height;
        int startWidth = width;        

        Output output = new Output(input);

        GridPoint gp = new GridPoint(x, y);
        output.embedding.add(gp); // start point

        Map<String, List<Integer>> indexList = inwardArrowsIndex(input, width-x, 0, Direction.UP, Direction.DOWN, inwardStep);
        List<Integer> indexListIn = indexList.get("Index List In");
        List<Integer> indexListOut = indexList.get("Index List Out");
        

        for (int i = 0; i < length; i++) {
            // change coordinates in the dircetion
            //System.out.println(i);
            if (direction == 0) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    y++;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    y--;
                    indexListOut.remove(0);
                } else {
                    x++;
                }
            } else if (direction == 1) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    x--;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    x++;
                    indexListOut.remove(0);
                } else {
                    y++;
                }
            } else if (direction == 2) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    y--;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    y++;
                    indexListOut.remove(0);
                } else {
                    x--;
                }
            }  else if (direction == 3) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    x++;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    x--;
                    indexListOut.remove(0);
                } else {
                    y--;
                }
            } 

            // check if the arrow hits the outer bound of the spiral
            if (direction == 0 && x == width) {
                direction = 1;
                width = width - inwardStep;
                indexList.clear();
                indexListIn.clear();
                indexListOut.clear();
                indexList = inwardArrowsIndex(input, width+i, i, Direction.LEFT, Direction.RIGHT, inwardStep);
                indexListIn = indexList.get("Index List In");
                indexListOut = indexList.get("Index List Out");
            } else if (direction == 1 && y == height) {
                direction = 2;
                height = height - inwardStep;
                indexList.clear();
                indexListIn.clear();
                indexListOut.clear();
                indexList = inwardArrowsIndex(input, height+i, i, Direction.DOWN, Direction.UP, inwardStep);
                indexListIn = indexList.get("Index List In");
                indexListOut = indexList.get("Index List Out");
            } else if (direction == 2 && x == startWidth - width - inwardStep) {
                direction = 3;
                indexList.clear();
                indexListIn.clear();
                indexListOut.clear();
                indexList = inwardArrowsIndex(input, width+i, i, Direction.RIGHT, Direction.LEFT, inwardStep);
                indexListIn = indexList.get("Index List In");
                indexListOut = indexList.get("Index List Out");
            } else if (direction == 3 && y == startHeight - height) {
                direction = 0;
                indexList.clear();
                indexListIn.clear();
                indexListOut.clear();
                indexList = inwardArrowsIndex(input, height+i, i, Direction.UP, Direction.DOWN, inwardStep);
                indexListIn = indexList.get("Index List In");
                indexListOut = indexList.get("Index List Out");
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

        Map<String, List<Integer>> indexList = inwardArrowsIndex(input, x-width, 0, Direction.DOWN, Direction.UP, inwardStep);
        List<Integer> indexListIn = indexList.get("Index List In");
        List<Integer> indexListOut = indexList.get("Index List Out");

        for (int i = 0; i < length; i++) {
            // change coordinates in the dircetion
            //System.out.println(i);
            if (direction == 0) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    y++;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    y--;
                    indexListOut.remove(0);
                } else {
                    x++;
                }
            } else if (direction == 1) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    x--;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    x++;
                    indexListOut.remove(0);
                } else {
                    y++;
                }
            } else if (direction == 2) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    y--;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    y++;
                    indexListOut.remove(0);
                } else {
                    x--;
                }
            }  else if (direction == 3) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    x++;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    x--;
                    indexListOut.remove(0);
                } else {
                    y--;
                }
            } 

            if (direction == 0 && x == width+inwardStep) {
                direction = 1;
                indexList.clear();
                indexList = inwardArrowsIndex(input, width+i, i, Direction.LEFT, Direction.RIGHT, inwardStep);
                indexListIn = indexList.get("Index List In");
                indexListOut = indexList.get("Index List Out");
            } else if (direction == 1 && y == height) {
                direction = 2;
                indexList.clear();
                indexList = inwardArrowsIndex(input, height+i, i, Direction.DOWN, Direction.UP, inwardStep);
                indexListIn = indexList.get("Index List In");
                indexListOut = indexList.get("Index List Out");
            } else if (direction == 2 && x == startWidth - width) {
                direction = 3;
                width = width - inwardStep;
                indexList.clear();
                indexList = inwardArrowsIndex(input, width+i, i, Direction.RIGHT, Direction.LEFT, inwardStep);
                indexListIn = indexList.get("Index List In");
                indexListOut = indexList.get("Index List Out");
            } else if (direction == 3 && y == startHeight - height) {
                direction = 0;
                height = height - inwardStep;
                indexList.clear();
                indexList = inwardArrowsIndex(input, height+i, i, Direction.UP, Direction.DOWN, inwardStep);
                indexListIn = indexList.get("Index List In");
                indexListOut = indexList.get("Index List Out");
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

        Map<String, List<Integer>> indexList = inwardArrowsIndex(input, y-width, 0, Direction.UP, Direction.DOWN, inwardStep);
        List<Integer> indexListIn = indexList.get("Index List In");
        List<Integer> indexListOut = indexList.get("Index List Out");

        for (int i = 0; i < length; i++) {
            // change coordinates in the dircetion
            //System.out.println(i);
            if (direction == 0) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    y++;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    y--;
                    indexListOut.remove(0);
                } else {
                    x++;
                }
            } else if (direction == 1) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    x--;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    x++;
                    indexListOut.remove(0);
                } else {
                    y++;
                }
            } else if (direction == 2) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    y--;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    y++;
                    indexListOut.remove(0);
                } else {
                    x--;
                }
            }  else if (direction == 3) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    x++;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    x--;
                    indexListOut.remove(0);
                } else {
                    y--;
                }
            } 

            if (direction == 0 && x == width) {
                direction = 1;
                width = width - inwardStep;
                indexList.clear();
                indexList = inwardArrowsIndex(input, width+i, i, Direction.LEFT, Direction.RIGHT, inwardStep);
                indexListIn = indexList.get("Index List In");
indexListOut = indexList.get("Index List Out");
            } else if (direction == 1 && y == height+inwardStep) {
                direction = 2;
                indexList.clear();
                indexList = inwardArrowsIndex(input, height+i, i, Direction.DOWN, Direction.UP, inwardStep);
                indexListIn = indexList.get("Index List In");
indexListOut = indexList.get("Index List Out");
            } else if (direction == 2 && x == startWidth - width) {
                direction = 3;
                indexList.clear();
                indexList = inwardArrowsIndex(input, width+i, i, Direction.RIGHT, Direction.LEFT, inwardStep);
                indexListIn = indexList.get("Index List In");
indexListOut = indexList.get("Index List Out");
            } else if (direction == 3 && y == startHeight - height) {
                direction = 0;
                height = height - inwardStep;
                indexList.clear();
                indexList = inwardArrowsIndex(input, height+i, i, Direction.UP, Direction.DOWN, inwardStep);
                indexListIn = indexList.get("Index List In");
indexListOut = indexList.get("Index List Out");
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

        Map<String, List<Integer>> indexList = inwardArrowsIndex(input, width-y, 0, Direction.LEFT, Direction.RIGHT, inwardStep);
        List<Integer> indexListIn = indexList.get("Index List In");
        List<Integer> indexListOut = indexList.get("Index List Out");

        for (int i = 0; i < length; i++) {
            // change coordinates in the dircetion
            //System.out.println(i);
            if (direction == 0) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    y++;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    y--;
                    indexListOut.remove(0);
                } else {
                    x++;
                }
            } else if (direction == 1) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    x--;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    x++;
                    indexListOut.remove(0);
                } else {
                    y++;
                }
            } else if (direction == 2) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    y--;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    y++;
                    indexListOut.remove(0);
                } else {
                    x--;
                }
            }  else if (direction == 3) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    x++;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    x--;
                    indexListOut.remove(0);
                } else {
                    y--;
                }
            } 

            // check if the arrow hits the outer bound of the spiral
            if (direction == 0 && x == width) {
                direction = 1;
                indexList.clear();
                indexList = inwardArrowsIndex(input, width+i, i, Direction.LEFT, Direction.RIGHT, inwardStep);
                indexListIn = indexList.get("Index List In");
indexListOut = indexList.get("Index List Out");
            } else if (direction == 1 && y == height) {
                direction = 2;
                height = height - inwardStep;
                indexList.clear();
                indexList = inwardArrowsIndex(input, height+i, i, Direction.DOWN, Direction.UP, inwardStep);
                indexListIn = indexList.get("Index List In");
indexListOut = indexList.get("Index List Out");
            } else if (direction == 2 && x == startWidth - width) {
                direction = 3;
                width = width - inwardStep;
                indexList.clear();
                indexList = inwardArrowsIndex(input, width+i, i, Direction.RIGHT, Direction.LEFT, inwardStep);
                indexListIn = indexList.get("Index List In");
indexListOut = indexList.get("Index List Out");
            } else if (direction == 3 && y == startHeight - height-inwardStep) {
                direction = 0;
                indexList.clear();
                indexList = inwardArrowsIndex(input, height+i, i, Direction.UP, Direction.DOWN, inwardStep);
                indexListIn = indexList.get("Index List In");
indexListOut = indexList.get("Index List Out");
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
    
    public Output pathDirectionRightAntiClockwise(Input input, int x, int y, int inwardStep, int length, int startDirection, int width, int height) {
        int direction = startDirection;
        int startHeight = height;
        int startWidth = width;        

        Output output = new Output(input);

        GridPoint gp = new GridPoint(x, y);
        output.embedding.add(gp); // start point

        Map<String, List<Integer>> indexList = inwardArrowsIndex(input, width-x, 0, Direction.DOWN, Direction.UP, inwardStep);
        List<Integer> indexListIn = indexList.get("Index List In");
        List<Integer> indexListOut = indexList.get("Index List Out");
        

        for (int i = 0; i < length; i++) {
            // change coordinates in the dircetion
            //System.out.println(i);
            if (direction == 0) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    y--;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    y++;
                    indexListOut.remove(0);
                } else {
                    x++;
                }
            } else if (direction == 1) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    x++;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    x--;
                    indexListOut.remove(0);
                } else {
                    y++;
                }
            } else if (direction == 2) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    y++;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    y--;
                    indexListOut.remove(0);
                } else {
                    x--;
                }
            }  else if (direction == 3) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    x--;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    x++;
                    indexListOut.remove(0);
                } else {
                    y--;
                }
            } 

            // check if the arrow hits the outer bound of the spiral
            if (direction == 0 && x == width) {
                direction = 3;
                width = width - inwardStep;
                indexList.clear();
                indexListIn.clear();
                indexListOut.clear();
                indexList = inwardArrowsIndex(input, width+i, i, Direction.RIGHT, Direction.LEFT, inwardStep);
                indexListIn = indexList.get("Index List In");
                indexListOut = indexList.get("Index List Out");
            } else if (direction == 1 && y == height) {
                direction = 0;
                indexList.clear();
                indexListIn.clear();
                indexListOut.clear();
                indexList = inwardArrowsIndex(input, height+i, i, Direction.UP, Direction.DOWN, inwardStep);
                indexListIn = indexList.get("Index List In");
                indexListOut = indexList.get("Index List Out");
            } else if (direction == 2 && x == startWidth - width - inwardStep) {
                direction = 1;
                indexList.clear();
                indexListIn.clear();
                indexListOut.clear();
                indexList = inwardArrowsIndex(input, width+i, i, Direction.LEFT, Direction.RIGHT, inwardStep);
                indexListIn = indexList.get("Index List In");
                indexListOut = indexList.get("Index List Out");
            } else if (direction == 3 && y == startHeight - height) {
                direction = 2;
                height = height - inwardStep;
                indexList.clear();
                indexListIn.clear();
                indexListOut.clear();
                indexList = inwardArrowsIndex(input, height+i, i, Direction.DOWN, Direction.UP, inwardStep);
                indexListIn = indexList.get("Index List In");
                indexListOut = indexList.get("Index List Out");
            }

            // add the grid point
            gp = new GridPoint(x, y);
            output.embedding.add(gp);
    }
        return output;
    }
    
    public Output pathDirectionLeftAntiClockwise(Input input, int x, int y, int inwardStep, int length, int startDirection, int width, int height) {
        int direction = startDirection;
        int startHeight = height;
        int startWidth = width;        

        Output output = new Output(input);

        GridPoint gp = new GridPoint(x, y);
        output.embedding.add(gp); // start point

        Map<String, List<Integer>> indexList = inwardArrowsIndex(input, x-width, 0, Direction.UP, Direction.DOWN, inwardStep);
        List<Integer> indexListIn = indexList.get("Index List In");
        List<Integer> indexListOut = indexList.get("Index List Out");
        

        for (int i = 0; i < length; i++) {
            // change coordinates in the dircetion
            //System.out.println(i);
            if (direction == 0) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    y--;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    y++;
                    indexListOut.remove(0);
                } else {
                    x++;
                }
            } else if (direction == 1) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    x++;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    x--;
                    indexListOut.remove(0);
                } else {
                    y++;
                }
            } else if (direction == 2) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    y++;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    y--;
                    indexListOut.remove(0);
                } else {
                    x--;
                }
            }  else if (direction == 3) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    x--;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    x++;
                    indexListOut.remove(0);
                } else {
                    y--;
                }
            } 

            // check if the arrow hits the outer bound of the spiral
            if (direction == 0 && x == width+inwardStep) {
                direction = 3;
                indexList.clear();
                indexListIn.clear();
                indexListOut.clear();
                indexList = inwardArrowsIndex(input, width+i, i, Direction.RIGHT, Direction.LEFT, inwardStep);
                indexListIn = indexList.get("Index List In");
                indexListOut = indexList.get("Index List Out");
            } else if (direction == 1 && y == height) {
                direction = 0;
                height = height - inwardStep;
                indexList.clear();
                indexListIn.clear();
                indexListOut.clear();
                indexList = inwardArrowsIndex(input, height+i, i, Direction.UP, Direction.DOWN, inwardStep);
                indexListIn = indexList.get("Index List In");
                indexListOut = indexList.get("Index List Out");
            } else if (direction == 2 && x == startWidth - width) {
                direction = 1;
                width = width - inwardStep;
                indexList.clear();
                indexListIn.clear();
                indexListOut.clear();
                indexList = inwardArrowsIndex(input, width+i, i, Direction.LEFT, Direction.RIGHT, inwardStep);
                indexListIn = indexList.get("Index List In");
                indexListOut = indexList.get("Index List Out");
            } else if (direction == 3 && y == startHeight - height) {
                direction = 2;
                indexList.clear();
                indexListIn.clear();
                indexListOut.clear();
                indexList = inwardArrowsIndex(input, height+i, i, Direction.DOWN, Direction.UP, inwardStep);
                indexListIn = indexList.get("Index List In");
                indexListOut = indexList.get("Index List Out");
            }

            // add the grid point
            gp = new GridPoint(x, y);
            output.embedding.add(gp);
    }
        return output;
    }
    
    public Output pathDirectionUpAntiClockwise(Input input, int x, int y, int inwardStep, int length, int startDirection, int width, int height) {
        int direction = startDirection;
        int startHeight = height;
        int startWidth = width;        

        Output output = new Output(input);

        GridPoint gp = new GridPoint(x, y);
        output.embedding.add(gp); // start point

        Map<String, List<Integer>> indexList = inwardArrowsIndex(input, height-x, 0, Direction.RIGHT, Direction.LEFT, inwardStep);
        List<Integer> indexListIn = indexList.get("Index List In");
        List<Integer> indexListOut = indexList.get("Index List Out");
        

        for (int i = 0; i < length; i++) {
            // change coordinates in the dircetion
            //System.out.println(i);
            if (direction == 0) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    y--;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    y++;
                    indexListOut.remove(0);
                } else {
                    x++;
                }
            } else if (direction == 1) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    x++;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    x--;
                    indexListOut.remove(0);
                } else {
                    y++;
                }
            } else if (direction == 2) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    y++;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    y--;
                    indexListOut.remove(0);
                } else {
                    x--;
                }
            }  else if (direction == 3) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    x--;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    x++;
                    indexListOut.remove(0);
                } else {
                    y--;
                }
            } 

            // check if the arrow hits the outer bound of the spiral
            if (direction == 0 && x == width) {
                direction = 3;
                width = width - inwardStep;
                indexList.clear();
                indexListIn.clear();
                indexListOut.clear();
                indexList = inwardArrowsIndex(input, width+i, i, Direction.RIGHT, Direction.LEFT, inwardStep);
                indexListIn = indexList.get("Index List In");
                indexListOut = indexList.get("Index List Out");
            } else if (direction == 1 && y == height) {
                direction = 0;
                height = height - inwardStep;
                indexList.clear();
                indexListIn.clear();
                indexListOut.clear();
                indexList = inwardArrowsIndex(input, height+i, i, Direction.UP, Direction.DOWN, inwardStep);
                indexListIn = indexList.get("Index List In");
                indexListOut = indexList.get("Index List Out");
            } else if (direction == 2 && x == startWidth - width) {
                direction = 1;
                indexList.clear();
                indexListIn.clear();
                indexListOut.clear();
                indexList = inwardArrowsIndex(input, width+i, i, Direction.LEFT, Direction.RIGHT, inwardStep);
                indexListIn = indexList.get("Index List In");
                indexListOut = indexList.get("Index List Out");
            } else if (direction == 3 && y == startHeight - height-inwardStep) {
                direction = 2;
                indexList.clear();
                indexListIn.clear();
                indexListOut.clear();
                indexList = inwardArrowsIndex(input, height+i, i, Direction.DOWN, Direction.UP, inwardStep);
                indexListIn = indexList.get("Index List In");
                indexListOut = indexList.get("Index List Out");
            }

            // add the grid point
            gp = new GridPoint(x, y);
            output.embedding.add(gp);
    }
        return output;
    }
    
    public Output pathDirectionDownAntiClockwise(Input input, int x, int y, int inwardStep, int length, int startDirection, int width, int height) {
        int direction = startDirection;
        int startHeight = height;
        int startWidth = width;        

        Output output = new Output(input);

        GridPoint gp = new GridPoint(x, y);
        output.embedding.add(gp); // start point

        Map<String, List<Integer>> indexList = inwardArrowsIndex(input, x-height, 0, Direction.LEFT, Direction.RIGHT, inwardStep);
        List<Integer> indexListIn = indexList.get("Index List In");
        List<Integer> indexListOut = indexList.get("Index List Out");
        

        for (int i = 0; i < length; i++) {
            // change coordinates in the dircetion
            //System.out.println(i);
            if (direction == 0) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    y--;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    y++;
                    indexListOut.remove(0);
                } else {
                    x++;
                }
            } else if (direction == 1) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    x++;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    x--;
                    indexListOut.remove(0);
                } else {
                    y++;
                }
            } else if (direction == 2) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    y++;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    y--;
                    indexListOut.remove(0);
                } else {
                    x--;
                }
            }  else if (direction == 3) {
                if (indexListIn.size() > 0 && i == indexListIn.get(0)) {
                    x--;
                    indexListIn.remove(0);
                } else if (indexListOut.size() > 0 && i == indexListOut.get(0)) {
                    x++;
                    indexListOut.remove(0);
                } else {
                    y--;
                }
            } 

            // check if the arrow hits the outer bound of the spiral
            if (direction == 0 && x == width) {
                direction = 3;
                indexList.clear();
                indexListIn.clear();
                indexListOut.clear();
                indexList = inwardArrowsIndex(input, width+i, i, Direction.RIGHT, Direction.LEFT, inwardStep);
                indexListIn = indexList.get("Index List In");
                indexListOut = indexList.get("Index List Out");
            } else if (direction == 1 && y == height + inwardStep) {
                direction = 0;
                indexList.clear();
                indexListIn.clear();
                indexListOut.clear();
                indexList = inwardArrowsIndex(input, height+i, i, Direction.UP, Direction.DOWN, inwardStep);
                indexListIn = indexList.get("Index List In");
                indexListOut = indexList.get("Index List Out");
            } else if (direction == 2 && x == startWidth - width) {
                direction = 1;
                width = width - inwardStep;
                indexList.clear();
                indexListIn.clear();
                indexListOut.clear();
                indexList = inwardArrowsIndex(input, width+i, i, Direction.LEFT, Direction.RIGHT, inwardStep);
                indexListIn = indexList.get("Index List In");
                indexListOut = indexList.get("Index List Out");
            } else if (direction == 3 && y == startHeight - height) {
                direction = 2;
                height = height - inwardStep;
                indexList.clear();
                indexListIn.clear();
                indexListOut.clear();
                indexList = inwardArrowsIndex(input, height+i, i, Direction.DOWN, Direction.UP, inwardStep);
                indexListIn = indexList.get("Index List In");
                indexListOut = indexList.get("Index List Out");
            }

            // add the grid point
            gp = new GridPoint(x, y);
            output.embedding.add(gp);
    }
        return output;
    }

    public Output determinePathAntiClockwise(Input input, int xStart, int yStart, int inwardStep) {

        Output output = new Output(input);
        int length = input.directions.size(); // the amount of arrows
        int width = input.width; // the width of the current spiral
        int height = input.height; // the height of the current spiral
    
        int direction = 4; // 0: right, 1: up, 2: left, 3: down, 4: nothing
        int x = xStart, y = yStart;
    
        // calc start direction
        if (y == width && x != width) {
            direction = 0;
            output = pathDirectionRightAntiClockwise(input, x, y, inwardStep, length, direction, width, height);
        } else if (x == width && y != 0) {
            direction = 3;
            output = pathDirectionDownAntiClockwise(input, x, y, inwardStep, length, direction, width, height);
        } else if (y == 0 && x != 0) {
            direction = 2;
            output = pathDirectionLeftAntiClockwise(input, x, y, inwardStep, length, direction, width, height);
        } else if (x == 0 && y != width) {
            direction = 1;
            output = pathDirectionUpAntiClockwise(input, x, y, inwardStep, length, direction, width, height);
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

        for (int x = 0; x < width+1; x++) {
            for (int y = 0; y < width+1; y++) {
                if (x == 0 || x == width || y == 0 || y == width) {
                    output = determinePathAntiClockwise(input, x, y, inwardStep);
                    if (output.computeQuality() < bestPenalty && output.isValid()) {
                        bestPenalty = output.computeQuality();
                        bestOutput = output;
                    }
                }
            }
        }
        return bestOutput;
    }

    @Override
    public Output doAlgorithm(Input input) {
        int inwardStep = calcInwardStep(input);
        Output output = createPath(input, inwardStep);
        //Output output = determinePath(input, input.width, input.width, inwardStep);
    return output;
    }
    
        
}

