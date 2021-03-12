package org.trypticon.talker.settings;

import java.awt.*;

import com.google.common.collect.ImmutableList;
import org.trypticon.talker.model.ConnectorType;

public class NodeBuilder {
    private final SettingsGraph graph;
    private final String title;

    private Point initialLocation;
    private final ImmutableList.Builder<ConnectorView> inputConnectorsBuilder = ImmutableList.builder();
    private final ImmutableList.Builder<ConnectorView> outputConnectorsBuilder = ImmutableList.builder();

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
        inputConnectorsBuilder.add(new ConnectorView(graph, ConnectorDirection.INPUT, type));
        return this;
    }

    public NodeBuilder outputConnector(ConnectorType type) {
        outputConnectorsBuilder.add(new ConnectorView(graph, ConnectorDirection.OUTPUT, type));
        return this;
    }

    public NodeView build() {
        return new NodeView(graph, title, initialLocation, inputConnectorsBuilder.build(),
                outputConnectorsBuilder.build());
    }
}
