package org.trypticon.talker.model;

import javax.annotation.Nonnull;

/**
 * Base class for both input and output connectors.
 */
public abstract class Connector {

    private Node parent;

    private final String id;

    // XXX: This string should be localised so we'll probably want some framework for that.
    private final String name;

    private final ConnectorType type;

    protected Connector(String id, String name, ConnectorType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    /**
     * Called to set the parent node. Can only be called once.
     *
     * @param parent the parent.
     * @throws IllegalStateException if the parent has already been set.
     */
    public void initParent(@Nonnull Node parent) {
        if (this.parent != null) {
            throw new IllegalStateException("Parent already set to " + this.parent + ", trying to set: " + parent);
        }
        this.parent = parent;
    }

    /**
     * Gets the parent node.
     *
     * @return the parent node.
     * @throws IllegalStateException if the parent has not been set.
     */
    @Nonnull
    public Node getParent() {
        if (parent == null) {
            throw new IllegalStateException("The parent has not yet been set!");
        }
        return parent;
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
