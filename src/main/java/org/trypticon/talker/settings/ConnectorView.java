package org.trypticon.talker.settings;

import java.awt.*;
import java.util.stream.Stream;
import javax.swing.*;

import org.trypticon.talker.model.ConnectorType;
import org.trypticon.talker.swing.DragLayout;

public class ConnectorView extends JPanel {
    private final SettingsGraph graph;
    private final ConnectorType type;
    private final ConnectorDirection direction;

    public ConnectorView(SettingsGraph graph, ConnectorDirection direction, ConnectorType type) {
        this.graph = graph;
        this.direction = direction;
        this.type = type;

        setBackground(type.getColor());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public boolean canConnectTo(ConnectorView overConnector) {
        return type == overConnector.type &&
                direction != overConnector.direction;
    }

    public ConnectionView connectTo(ConnectorView otherConnector, double cableLength) {
        // Are we the source or the target?
        ConnectorView source;
        ConnectorView target;
        if (direction == ConnectorDirection.OUTPUT) {
            source = this;
            target = otherConnector;
        } else {
            source = otherConnector;
            target = this;
        }
        // TODO: What if it's already connected?
        ConnectionView newConnection = new ConnectionView(graph, source, target, cableLength);
        graph.add(newConnection);
        return newConnection;
    }

    public Stream<ConnectionView> getConnections() {
        return graph.getConnectionsTo(this);
    }

    public ConnectorView createCloneForDrag() {
        // XXX: Direction perhaps dubious for this connector that isn't even connected.
        ConnectorView clone = new ConnectorView(graph, direction, type);

        // Move a copy of the clone to the drag panel so that we can actually drag it around.
        // We have to fiddle the location to be relative to the right panel as well.
        graph.add(clone);
        graph.setComponentZOrder(clone, 0);

//        Connection connection = new Connection(graph, this, clone, 0.0);
//        graph.add(connection);

        Point graphLocation = graph.getLocationOnScreen();
        Point ourLocation = getLocationOnScreen();
        Rectangle cloneBounds = getBounds();
        cloneBounds.translate(ourLocation.x - graphLocation.x, ourLocation.y - graphLocation.y);
        clone.setBounds(cloneBounds);

        return clone;
    }

    private Container findDragPanel() {
        Container container = getParent();
        while (container != null) {
            if (container.getLayout() instanceof DragLayout) {
                return container;
            }
            container = container.getParent();
        }
        throw new IllegalStateException("Should not have called this method while not in the component tree");
    }

}
