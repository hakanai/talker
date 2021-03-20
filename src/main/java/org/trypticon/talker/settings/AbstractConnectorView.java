package org.trypticon.talker.settings;

import java.awt.*;
import java.util.stream.Stream;
import javax.swing.*;

import org.trypticon.talker.model.Connector;
import org.trypticon.talker.model.InputConnector;

abstract class AbstractConnectorView<C extends Connector> extends JPanel {
    private final GraphView graphView;
    private final C connector;

    private static final Stroke outlineStroke = new BasicStroke(2.0f);

    AbstractConnectorView(GraphView graphView, C connector) {
        this.graphView = graphView;
        this.connector = connector;

        setForeground(Color.BLACK);
        setBackground(connector.getType().getColor());
    }

    public final GraphView getGraphView() {
        return graphView;
    }

    public C getConnector() {
        return connector;
    }

    public Stream<ConnectionView> getConnections() {
        return graphView.getConnectionsTo(this);
    }

    /**
     * Creates a fake input connector with the same properties as the output connector
     * so that it can be used as a draggable connector.
     *
     * @return the draggable connector.
     */
    public final InputConnectorView createCloneForDrag() {
        GraphView graphView = getGraphView();

        // It will only let us create a connection between an output and an input,
        // so we have to create an ephemeral input, but we'll just create it with the
        // same properties as the output.
        Connector connector = getConnector();
        InputConnector ephemeralConnector = new InputConnector(connector.getId(), connector.getName(),
                connector.getType());
        InputConnectorView clone = new InputConnectorView(graphView, ephemeralConnector);

        // Move a copy of the clone to the drag panel so that we can actually drag it around.
        // We have to fiddle the location to be relative to the right panel as well.
        graphView.add(clone);
        graphView.setComponentZOrder(clone, 0);

        Point graphLocation = graphView.getLocationOnScreen();
        Point ourLocation = getLocationOnScreen();
        Rectangle cloneBounds = getBounds();
        cloneBounds.setLocation(ourLocation.x - graphLocation.x, ourLocation.y - graphLocation.y);
        clone.setBounds(cloneBounds);

        return clone;
    }

    @Override
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet()) {
            return getPreferredSize();
        }
        return new Dimension(18, 18);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(outlineStroke);
        g2d.setColor(getBackground());
        g2d.fillOval(1, 1, getWidth() - 3, getHeight() - 3);
        g2d.setColor(getForeground());
        g2d.drawOval(1, 1, getWidth() - 3, getHeight() - 3);
    }
}
