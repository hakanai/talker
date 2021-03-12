package org.trypticon.talker.model;

import java.util.stream.Stream;

import com.jgoodies.common.collect.ArrayListModel;
import com.jgoodies.common.collect.ObservableList;

public class Graph implements Startable {
    private final ObservableList<Node> nodes = new ArrayListModel<>();
    private final ObservableList<Connection> connections = new ArrayListModel<>();

    public void add(Node node) {
        nodes.add(node);
    }

    public ObservableList<Node> getNodes() {
        return nodes;
    }

    public void add(Connection connection) {
        connections.add(connection);
    }

    public ObservableList<Connection> getConnections() {
        return connections;
    }

    public Stream<Connection> getConnectionsFor(OutputConnector outputConnector) {
        return connections.stream()
                .filter(connection -> connection.getSource() == outputConnector);
    }

    @Override
    public void start() {
        startableNodes().forEach(Startable::start);
    }

    @Override
    public void stop() {
        startableNodes().forEach(Startable::stop);
    }

    private Stream<Startable> startableNodes() {
        return nodes.stream()
                .filter(n -> n instanceof Startable)
                .map(n -> (Startable) n);
    }
}
