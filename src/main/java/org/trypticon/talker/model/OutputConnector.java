package org.trypticon.talker.model;

/**
 * Model for a single connector.
 */
public class OutputConnector extends Connector {
    public OutputConnector(String id, String name, ConnectorType type) {
        super(id, name, type);
    }

    private Graph getGraph() {
        return getParent().getGraph();
    }

    public void push(Object data) {
        // TODO: Probably should be in new background task for each?
        getGraph().getConnectionsFor(this).forEach(connection -> connection.push(data));
    }

}
