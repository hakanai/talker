package org.trypticon.talker.model;

import com.google.common.collect.ImmutableList;
import org.trypticon.talker.config.Configuration;

/**
 * Model for a single node.
 */
public abstract class Node {
    private final Graph graph;
    private final String providerId;
    private final String name;
    private final ImmutableList<InputConnector> inputConnectors;
    private final ImmutableList<OutputConnector> outputConnectors;

    public Node(Graph graph, String providerId, String name,
                ImmutableList<InputConnector> inputConnectors,
                ImmutableList<OutputConnector> outputConnectors) {

        this.graph = graph;
        this.providerId = providerId;
        this.name = name;
        this.inputConnectors = inputConnectors;
        this.outputConnectors = outputConnectors;

        inputConnectors.forEach(connector -> connector.initParent(this));
        outputConnectors.forEach(connector -> connector.initParent(this));
    }

    public abstract void push(InputConnector connector, Object data);

    protected final void sendOutput(OutputConnector connector, Object data) {
        connector.push(data);
    }

    public final Graph getGraph() {
        return graph;
    }

    public final String getProviderId() {
        return providerId;
    }

    public final String getName() {
        return name;
    }

    public abstract void populateConfiguration(Configuration.Builder builder);

    public final Configuration getConfiguration() {
        Configuration.Builder builder = Configuration.builder();
        populateConfiguration(builder);
        return builder.build();
    }

    public final ImmutableList<InputConnector> getInputConnectors() {
        return inputConnectors;
    }

    public final InputConnector getInputConnectorById(String id) {
        return getInputConnectors().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No connector named: " + id));
    }

    public final ImmutableList<OutputConnector> getOutputConnectors() {
        return outputConnectors;
    }

    public final OutputConnector getOutputConnectorById(String id) {
        return getOutputConnectors().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No connector named: " + id));
    }

}
