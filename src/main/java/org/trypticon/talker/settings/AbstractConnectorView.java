package org.trypticon.talker.settings;

import java.awt.*;
import java.util.stream.Stream;
import javax.swing.*;

import org.trypticon.talker.model.Connector;
import org.trypticon.talker.model.InputConnector;

abstract class AbstractConnectorView<C extends Connector> extends JPanel {
    private final GraphView graphView;
    private final C connector;

    AbstractConnectorView(GraphView graphView, C connector) {
        this.graphView = graphView;
        this.connector = connector;

        setBackground(connector.getType().getColor());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
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

    protected abstract AbstractConnectorView<C> createCloneForDragInner(GraphView graphView, Connector connector);

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

//        Connection connection = new Connection(graph, this, clone, 0.0);
//        graph.add(connection);

        Point graphLocation = graphView.getLocationOnScreen();
        Point ourLocation = getLocationOnScreen();
        Rectangle cloneBounds = getBounds();
        cloneBounds.translate(ourLocation.x - graphLocation.x, ourLocation.y - graphLocation.y);
        clone.setBounds(cloneBounds);

        return clone;
    }
}
