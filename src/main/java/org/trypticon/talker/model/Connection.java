package org.trypticon.talker.model;

public class Connection {
    private final OutputConnector source;
    private final InputConnector destination;

    public Connection(OutputConnector source, InputConnector destination) {
        this.source = source;
        this.destination = destination;
    }

    public OutputConnector getSource() {
        return source;
    }

    public InputConnector getDestination() {
        return destination;
    }

    public void push(Object data) {
        destination.push(data);
    }
}
