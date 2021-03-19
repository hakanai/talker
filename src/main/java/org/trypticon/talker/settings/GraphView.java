package org.trypticon.talker.settings;

import java.awt.*;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.*;

import org.trypticon.talker.model.Connector;
import org.trypticon.talker.model.Graph;
import org.trypticon.talker.model.InputConnector;
import org.trypticon.talker.model.OutputConnector;
import org.trypticon.talker.swing.DragLayout;

public class GraphView extends JComponent {

    private final List<ConnectionView> connections = new ArrayList<>();

    private Graph graph;

    public GraphView() {
        setLayout(new DragLayout());

        addContainerListener(new ContainerAdapter() {
            @Override
            public void componentRemoved(ContainerEvent event) {
                Component child = event.getChild();
                if (child instanceof AbstractConnectorView<?>) {
                    removeAll(getConnectionsTo((AbstractConnectorView<?>) child)
                            .collect(Collectors.toUnmodifiableList()));
                }
            }
        });
    }

    private void initViewFromGraph() {
        removeAll();
        connections.clear();

        // Keeping track of the connectors as we add the nodes so that we can look them
        // up as we add the connections.
        Map<Connector, AbstractConnectorView<?>> viewsByConnector = new LinkedHashMap<>();

        graph.getNodes().forEach(node -> {
            NodeView nodeView = new NodeView(this, node);
            for (InputConnectorView connectorView : nodeView.getInputConnectors()) {
                viewsByConnector.put(connectorView.getConnector(), connectorView);
            }
            for (OutputConnectorView connectorView : nodeView.getOutputConnectors()) {
                viewsByConnector.put(connectorView.getConnector(), connectorView);
            }
            add(nodeView);
        });

        graph.getConnections().forEach(connection -> {
            OutputConnector sourceModel = connection.getSource();
            InputConnector targetModel = connection.getTarget();
            OutputConnectorView source = (OutputConnectorView) viewsByConnector.get(sourceModel);
            InputConnectorView target = (InputConnectorView) viewsByConnector.get(targetModel);
            int cableLength = connection.getCableLength();
            add(new ConnectionView(this, source, target, cableLength));
        });

        validate();
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
        initViewFromGraph();
    }

    public void add(ConnectionView connection) {
        connections.add(connection);
        repaint();
    }

    public void remove(ConnectionView connection) {
        connections.remove(connection);
        repaint();
    }

    public void removeAll(Collection<? extends ConnectionView> connections) {
        this.connections.removeAll(connections);
    }

    public Stream<ConnectionView> getConnectionsTo(AbstractConnectorView<?> connector) {
        return connections.stream()
                .filter(connection -> connection.getSource() == connector ||
                        connection.getTarget() == connector);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // TODO: Paint background pattern here

        for (ConnectionView connection : connections) {
            connection.paintUnder(g);
        }

        // Nodes themselves are child components so they will be painted for us.
    }
}
