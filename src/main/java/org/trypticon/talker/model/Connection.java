package org.trypticon.talker.model;

public class Connection {
    private final OutputConnector source;
    private final InputConnector target;
    private final int cableLength;

    public Connection(OutputConnector source, InputConnector target, int cableLength) {
        this.source = source;
        this.target = target;
        this.cableLength = cableLength;
    }

    public OutputConnector getSource() {
        return source;
    }

    public InputConnector getTarget() {
        return target;
    }

    public int getCableLength() {
        return cableLength;
    }

    public void push(Object data) {
        target.push(data);
    }
}
