package org.trypticon.talker.settings;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.*;
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

    private final Paint background = new TexturePaint(
            createGridTexture(), new Rectangle2D.Float(0.0f, 0.0f, 24.0f, 24.0f));

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

            // Bind the node view position back to the model.
            nodeView.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentMoved(ComponentEvent event) {
                    node.setLocation(nodeView.getX(), nodeView.getY());
                }
            });

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

        revalidate();
    }

    public void loadGraphFromFile() {
        new LoadGraphWorker()
                .whenDone(this::setGraph)
                .execute();
    }

    public void saveGraphToFile() {
        new SaveGraphWorker(graph).execute();
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

        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(background);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        for (ConnectionView connection : connections) {
            connection.paintUnder(g2d);
        }

        // Nodes themselves are child components so they will be painted for us.
    }

    private static BufferedImage createGridTexture() {
        BufferedImage image = new BufferedImage(24, 24, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = image.createGraphics();
        try {
            g.setColor(new Color(0, 10, 10));
            g.fillRect(0, 0, 24, 24);
            g.setColor(new Color(0, 20, 20));
            g.setStroke(new BasicStroke(2.0f));
            g.drawLine(12, -2, 12, 26);
            g.drawLine(-2, 12, 26, 12);
        } finally {
            g.dispose();
        }
        return image;
    }
}
