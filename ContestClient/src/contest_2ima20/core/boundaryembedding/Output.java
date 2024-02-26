/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contest_2ima20.core.boundaryembedding;

import contest_2ima20.core.problem.Solution;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import nl.tue.geometrycore.geometry.Vector;
import nl.tue.geometrycore.geometry.linear.LineSegment;
import nl.tue.geometrycore.geometry.linear.PolyLine;
import nl.tue.geometrycore.geometry.linear.Rectangle;
import nl.tue.geometrycore.geometryrendering.GeometryRenderer;
import nl.tue.geometrycore.geometryrendering.glyphs.ArrowStyle;
import nl.tue.geometrycore.geometryrendering.styling.Dashing;
import nl.tue.geometrycore.geometryrendering.styling.ExtendedColors;
import nl.tue.geometrycore.geometryrendering.styling.Hashures;
import nl.tue.geometrycore.geometryrendering.styling.SizeMode;

/**
 *
 * @author Wouter Meulemans (w.meulemans@tue.nl)
 */
public class Output extends Solution {

    public Input input;
    public List<GridPoint> embedding;

    public Output(Input input) {
        this.input = input;
        embedding = new ArrayList();
    }

    public PolyLine toPolyLine() {
        PolyLine pl = new PolyLine();
        for (GridPoint u : embedding) {
            pl.addVertex(u.toGeometry());
        }
        return pl;
    }

    @Override
    public boolean isValid() {

        // appropriate length
        if (embedding.size() != input.directions.size() + 1) {
            return false;
        }

        // test bounds
        for (GridPoint gp : embedding) {
            int x = gp.getIntX();
            if (x < 0 || x > input.width) {
                return false;
            }

            int y = gp.getIntY();
            if (y < 0 || y > input.height) {
                return false;
            }
        }

        // test consecutiveness
        for (int i = 0; i < embedding.size() - 1; i++) {
            GridPoint u = embedding.get(i);
            GridPoint v = embedding.get(i + 1);

            if (!u.isAdjacentCoordinate(v)) {
                return false;
            }
        }

        // test uniqueness
        HashSet<GridPoint> usedpoints = new HashSet();
        for (int i = 0; i < embedding.size(); i++) {
            GridPoint u = embedding.get(i);
            if (usedpoints.contains(u)) {
                return false;
            } else {
                usedpoints.add(u);
            }
        }

        return true;
    }

    @Override
    public double computeQuality() {
        int deviations = 0;
        for (int i = 0; i < embedding.size() - 1; i++) {
            GridPoint u = embedding.get(i);
            GridPoint v = embedding.get(i + 1);
            if (u.getDirectionTo(v) != input.directions.get(i)) {
                deviations++;
            }
        }
        return deviations;
    }

    @Override
    public void write(Writer out) throws IOException {

        for (GridPoint u : embedding) {
            out.append(u.getIntX() + ";" + u.getIntY() + "\n");
        }
    }

    @Override
    public void draw(GeometryRenderer render) {
        input.draw(render);

        render.setSizeMode(SizeMode.VIEW);
        render.setForwardArrowStyle(ArrowStyle.TRIANGLE_SOLID, 6);
        render.setFill(null, Hashures.SOLID);
        for (int i = 0; i < embedding.size() - 1; i++) {
            GridPoint u = embedding.get(i);
            GridPoint v = embedding.get(i+1);
            if (Vector.subtract(v, u).isApproximately(input.directions.get(i).toVector())) {
                render.setStroke(ExtendedColors.darkBlue, 3, Dashing.SOLID);
            } else {
                render.setStroke(ExtendedColors.darkRed, 3, Dashing.SOLID);
            }
            render.draw(new LineSegment(u, v));
        }
    }

    @Override
    public Rectangle getBoundingBox() {
        return input.getBoundingBox();
    }
}
