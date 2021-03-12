package org.trypticon.talker.model;

/**
 * Model for a single connector.
 */
public class InputConnector extends Connector {
    private Node parent;

    public InputConnector(String id, String name, ConnectorType type) {
        super(id, name, type);
    }

    public void initParent(Node parent) {
        this.parent = parent;
    }

    void push(Object data) {
        parent.push(this, data);
    }
}
