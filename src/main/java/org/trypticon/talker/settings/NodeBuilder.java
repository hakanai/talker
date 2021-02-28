package org.trypticon.talker.settings;

import java.awt.*;

import com.google.common.collect.ImmutableList;

public class NodeBuilder {
    private final SettingsGraph graph;
    private final String title;

    private Point initialLocation;
    private final ImmutableList.Builder<Connector> inputConnectorsBuilder = ImmutableList.builder();
    private final ImmutableList.Builder<Connector> outputConnectorsBuilder = ImmutableList.builder();

    NodeBuilder(SettingsGraph graph, String title) {
        this.graph = graph;
        this.title = title;
    }

    public NodeBuilder initialLocation(int x, int y) {
        return initialLocation(new Point(x, y));
    }

    public NodeBuilder initialLocation(Point point) {
        initialLocation = point;
        return this;
    }

    public NodeBuilder inputConnector(ConnectorType type) {
        inputConnectorsBuilder.add(new Connector(graph, ConnectorDirection.INPUT, type));
        return this;
    }

    public NodeBuilder outputConnector(ConnectorType type) {
        outputConnectorsBuilder.add(new Connector(graph, ConnectorDirection.OUTPUT, type));
        return this;
    }

    public Node build() {
        return new Node(graph, title, initialLocation, inputConnectorsBuilder.build(),
                outputConnectorsBuilder.build());
    }
}
