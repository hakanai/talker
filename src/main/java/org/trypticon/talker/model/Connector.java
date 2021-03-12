package org.trypticon.talker.model;

/**
 * Base class for both input and output connectors.
 */
public abstract class Connector {

    private final String id;

    // XXX: This string should be localised so we'll probably want some framework for that.
    private final String name;

    private final ConnectorType type;

    protected Connector(String id, String name, ConnectorType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public final String getId() {
        return id;
    }

    public final String getName() {
        return name;
    }

    public final ConnectorType getType() {
        return type;
    }

}
