/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contest_2ima20.core.boundaryembedding;

import contest_2ima20.core.problem.Problem;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import nl.tue.geometrycore.geometry.Vector;
import nl.tue.geometrycore.geometry.linear.LineSegment;
import nl.tue.geometrycore.geometry.linear.Rectangle;
import nl.tue.geometrycore.geometryrendering.GeometryRenderer;
import nl.tue.geometrycore.geometryrendering.glyphs.ArrowStyle;
import nl.tue.geometrycore.geometryrendering.styling.Dashing;
import nl.tue.geometrycore.geometryrendering.styling.ExtendedColors;
import nl.tue.geometrycore.geometryrendering.styling.Hashures;
import nl.tue.geometrycore.geometryrendering.styling.SizeMode;
import nl.tue.geometrycore.util.DoubleUtil;

/**
 *
 * @author Wouter Meulemans (w.meulemans@tue.nl)
 */
public class Input extends Problem<Output> {

    public int width, height;
    public List<Direction> directions;

    @Override
    public void read(BufferedReader read) throws IOException {
        directions = new ArrayList();
        boolean[] first = {true};
        read.lines().forEach((line) -> {
            if (first[0]) {
                first[0] = false;
                String[] split = line.split(";");
                width = Integer.parseInt(split[0]);
                height = Integer.parseInt(split[1]);
            } else {
                line.chars().forEach((c) -> {
                    directions.add(Direction.parse(c));
                });
            }
        });
    }

    @Override
    public void write(Writer writer) throws IOException {

        writer.write(width + ";" + height + "\n");
        final int perline = 128;
        int k = perline;
        for (Direction d : directions) {
            writer.write(d.toString());
            k--;
            if (k == 0) {
                writer.write("\n");
                k = perline;
            }
        }
        writer.write("\n");
    }

    @Override
    public Output parseSolution(BufferedReader read) throws IOException {
        Output output = new Output(this);
        read.lines().forEach((line) -> {
            String[] splitline = line.split(";");
            int x = Integer.parseInt(splitline[0]);
            int y = Integer.parseInt(splitline[1]);
            output.embedding.add(new GridPoint(x, y));
        });

        return output;
    }

    @Override
    public void draw(GeometryRenderer render) {
        render.setSizeMode(SizeMode.VIEW);

        render.setStroke(ExtendedColors.black, 1, Dashing.SOLID);
        render.setForwardArrowStyle(null, 1);

        LineSegment vert = LineSegment.byStartAndOffset(Vector.origin(), Vector.up(height));
        for (int i = 0; i <= width; i++) {
            render.draw(vert);
            vert.translate(1, 0);
        }
        LineSegment horz = LineSegment.byStartAndOffset(Vector.origin(), Vector.right(width));
        for (int i = 0; i <= height; i++) {
            render.draw(horz);
            horz.translate(0, 1);
        }

        render.setStroke(Color.black, 3, Dashing.SOLID);
        render.setForwardArrowStyle(ArrowStyle.TRIANGLE_SOLID, 6);
        render.setFill(null, Hashures.SOLID);
        for (int i = 0; i < directions.size(); i++) {
            Vector dir = directions.get(i).toVector();
            dir.scale(0.45);
            Vector loc = new Vector(i, -2);
            render.draw(new LineSegment(Vector.subtract(loc, dir), Vector.add(loc, dir)));
        }
    }

    @Override
    public Rectangle getBoundingBox() {
        return new Rectangle(0, width, -3, height);
    }

    @Override
    public boolean isValidInstance() {
        return directions.size() > 0;
    }
}
