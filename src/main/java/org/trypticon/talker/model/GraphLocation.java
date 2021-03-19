package org.trypticon.talker.model;

import java.awt.*;

public class GraphLocation {
    private final Graph graph;
    private final int x;
    private final int y;

    public GraphLocation(Graph graph, Point location) {
        this(graph, location.x, location.y);
    }

    public GraphLocation(Graph graph, int x, int y) {
        this.graph = graph;
        this.x = x;
        this.y = y;
    }

    public Graph getGraph() {
        return graph;
    }

    public Point getLocation() {
        return new Point(x, y);
    }
}
