package org.trypticon.talker.model;

import com.google.common.collect.ImmutableList;

/**
 * Model for a single node.
 */
public abstract class Node {
    private String name;
    private ImmutableList<InputConnector> inputConnectors;
    private ImmutableList<OutputConnector> outputConnectors;

    public Node(String name,
                ImmutableList<InputConnector> inputConnectors,
                ImmutableList<OutputConnector> outputConnectors) {

        this.name = name;
        this.inputConnectors = inputConnectors;
        this.outputConnectors = outputConnectors;

        for (InputConnector connector : inputConnectors) {
            connector.initParent(this);
        }
    }

    public abstract void push(InputConnector connector, Object data);

    protected void sendOutput(OutputConnector connector, Object data) {
        connector.push(data);
    }

    public String getName() {
        return name;
    }

    public ImmutableList<InputConnector> getInputConnectors() {
        return inputConnectors;
    }

    public InputConnector getInputConnectorById(String id) {
        return getInputConnectors().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No connector named: " + id));
    }

    public ImmutableList<OutputConnector> getOutputConnectors() {
        return outputConnectors;
    }

    public OutputConnector getOutputConnectorById(String id) {
        return getOutputConnectors().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No connector named: " + id));
    }

}
