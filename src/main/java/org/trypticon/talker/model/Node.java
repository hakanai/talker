package org.trypticon.talker.model;

import java.awt.*;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.config.Configurator;

/**
 * Model for a single node.
 */
public abstract class Node {
    private final GraphLocation graphLocation;
    private final String providerId;
    private final String name;
    private final ImmutableList<InputConnector> inputConnectors;
    private final ImmutableList<OutputConnector> outputConnectors;

    public Node(GraphLocation graphLocation, String providerId, String name,
                ImmutableList<InputConnector> inputConnectors,
                ImmutableList<OutputConnector> outputConnectors) {

        this.graphLocation = graphLocation;
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
        return graphLocation.getGraph();
    }

    public Point getLocation() {
        return graphLocation.getLocation();
    }

    public final String getProviderId() {
        return providerId;
    }

    public final String getName() {
        return name;
    }

    public abstract void populateConfiguration(Configuration.Builder builder);

    /**
     * Overridden by nodes wishing to display custom UI for configuring the node.
     *
     * @return the configurator, or {@code null} if there is no configurator.
     */
    @Nullable
    public Configurator createConfigurator() {
        return null;
    }

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
