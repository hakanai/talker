package org.trypticon.talker.model;

/**
 * Model for a single connector.
 */
public class InputConnector extends Connector {
    public InputConnector(String id, String name, ConnectorType type) {
        super(id, name, type);
    }

    void push(Object data) {
        getParent().push(this, data);
    }
}
