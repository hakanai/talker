package org.trypticon.talker.settings;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import com.google.common.collect.ImmutableList;
import org.trypticon.talker.util.MoreMath;

/**
 * Helper for rendering catenary curves which is a big enough problem
 * all by itself and may one day get extracted to a library.
 *
 * Adapted from a JavaScript implementation by Jan Hug <me@dulnan.net>
 */
public class Catenary {
    private static final double EPSILON = 1e-6;
    private static final int DEFAULT_SEGMENT_COUNT = 50;
    private static final int DEFAULT_ITERATION_LIMIT = 100;

    private final Point2D p1;
    private final Point2D p2;

    private final int segments;
    private final int iterationLimit;

    private ImmutableList<Point2D> curveData;
    private boolean isStraight;
    private boolean isSlack;

    /**
     * Constructs the helper with default settings.
     */
    Catenary() {
        this(DEFAULT_SEGMENT_COUNT, DEFAULT_ITERATION_LIMIT);
    }

    /**
     * Constructs the helper.
     *
     * @param segments the number of segments of the chain.
     * @param iterationLimit the maximum iterations for getting catenary parameters.
     */
    Catenary(int segments, int iterationLimit) {
        this.p1 = new Point2D.Double();
        this.p2 = new Point2D.Double();

        this.segments = segments;
        this.iterationLimit = iterationLimit;
    }

    void update(Point2D point1, Point2D point2, double chainLength) {
        p1.setLocation(point1);
        p2.setLocation(point2);

        boolean isFlipped = this.p1.getX() > this.p2.getX();

        Point2D p1 = isFlipped ? this.p2 : this.p1;
        Point2D p2 = isFlipped ? this.p1 : this.p2;

        double distance = p1.distance(p2);

        // Prevent "expensive" catenary calculations if it would only result
        // in a straight line.
        if (distance < chainLength) {
            double diff = p2.getX() - p1.getX();

            // If the distance on the x axis of both points is too small, don't
            // calculate a catenary.
            if (diff > 0.01) {
                double h = p2.getX() - p1.getX();
                double v = p2.getY() - p1.getY();
                double a = -this.getCatenaryParameter(h, v, chainLength, this.iterationLimit);
                double x = (a * Math.log((chainLength + v) / (chainLength - v)) - h) * 0.5;
                double y = a * Math.cosh(x / a);
                double offsetX = p1.getX() - x;
                double offsetY = p1.getY() - y;
                curveData = this.getCurve(a, p1, p2, offsetX, offsetY, this.segments);
                isStraight = false;
                isSlack = true;
            } else {
                // Chain hangs straight down
                double mx = (p1.getX() + p2.getX()) * 0.5;
                double my = (p1.getY() + p2.getY() + chainLength) * 0.5;
                curveData = ImmutableList.of(p1, new Point2D.Double(mx, my), p2);
                isStraight = true;
                isSlack = true;
            }
        } else {
            curveData = ImmutableList.of(p1, p2);
            isStraight = true;
            isSlack = false;
        }
    }

    /**
     * Draws a catenary given two coordinates, a length and a context.
     *
     * TODO: Refactor to store the path?
     *
     * @param context the canvas context to draw the catenary on to.
     */
    void drawToCanvas(Graphics2D context) {
        if (curveData != null) {
            if (isStraight) {
                drawLineSegments(curveData, context);
            } else {
                drawCurves(curveData, context);
            }
        }
    }

    /**
     * Determines catenary parameter.
     *
     * @param h horizontal distance of both points.
     * @param v vertical distance of both points.
     * @param length the catenary length.
     * @param limit maximum amount of iterations to find parameter.
     */
    private double getCatenaryParameter(double h, double v, double length, double limit) {
        double m = Math.sqrt(length * length - v * v) / h;
        double x = MoreMath.acosh(m) + 1;
        double prevX = -1;
        int count = 0;

        while (Math.abs(x - prevX) > EPSILON && count < limit) {
            prevX = x;
            x = x - (Math.sinh(x) - m * x) / (Math.cosh(x) - m);
            count++;
        }

        return h / (2 * x);
    }

    /**
     * Calculate the catenary curve.
     * Increasing the segments value will produce a catenary closer
     * to reality, but will require more calcluations.
     *
     * @param a the catenary parameter.
     * @param p1 first point
     * @param p2 second point
     * @param offsetX the calculated offset on the x axis.
     * @param offsetY the calculated offset on the y axis.
     * @param segments how many "parts" the chain should be made of.
     */
    private ImmutableList<Point2D> getCurve(double a, Point2D p1, Point2D p2, double offsetX, double offsetY, int segments) {
        ImmutableList.Builder<Point2D> data = ImmutableList.builder();
        data.add(new Point2D.Double(p1.getX(), a * Math.cosh((p1.getX() - offsetX) / a) + offsetY));

        double d = p2.getX() - p1.getX();
        int length = segments - 1;

        for (int i = 0; i < length; i++) {
            double x = p1.getX() + d * (i + 0.5) / length;
            double y = a * Math.cosh((x - offsetX) / a) + offsetY;
            data.add(new Point2D.Double(x, y));
        }

        data.add(new Point2D.Double(p2.getX(), a * Math.cosh((p2.getX() - offsetX) / a) + offsetY));

        return data.build();
    }

    /**
     * Draws a straight line between two points.
     *
     * @param data array of x/y array pairs.
     * @param context the context to draw to.
     */
    private void drawLineSegments(ImmutableList<Point2D> data, Graphics2D context) {
        Path2D path = new GeneralPath();
        Point2D p1 = data.get(0);
        path.moveTo(p1.getX(), p1.getY());
        Point2D p2 = data.get(1);
        path.lineTo(p2.getX(), p2.getY());
        context.draw(path);
    }

    /**
     * Draws a quadratic curve between every calculated catenary segment,
     * so that the segments don't look like straight lines.
     *
     * @param data the array of points on the curve.
     * @param context the context to draw to.
     */
    private void drawCurves(ImmutableList<Point2D> data, Graphics2D context) {
        int dataSize = data.size();
        double ox = data.get(1).getX();
        double oy = data.get(1).getY();

        Path2D path = new GeneralPath();

        Point2D p0 = data.get(0);
        path.moveTo(p0.getX(), p0.getY());

        for (int i = 2; i < dataSize - 1; i++) {
            Point2D p = data.get(i);
            double x = p.getX();
            double y = p.getY();
            double mx = (x + ox) * 0.5;
            double my = (y + oy) * 0.5;
            path.quadTo(ox, oy, mx, my);
            ox = x;
            oy = y;
        }

        Point2D p1 = data.get(dataSize - 2);
        Point2D p2 = data.get(dataSize - 1);
        path.quadTo(p1.getX(), p1.getY(), p2.getX(), p2.getY());

        context.draw(path);
    }
}
