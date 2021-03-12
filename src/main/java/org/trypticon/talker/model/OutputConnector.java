package org.trypticon.talker.model;

/**
 * Model for a single connector.
 */
public class OutputConnector extends Connector {
    private final Graph graph;

    public OutputConnector(String id, String name, ConnectorType type, Graph graph) {
        super(id, name, type);

        this.graph = graph;
    }

    public void push(Object data) {
        // TODO: Probably should be in new background task for each?
        graph.getConnectionsFor(this).forEach(connection -> connection.push(data));
    }

}
