package org.trypticon.talker.settings;

import java.awt.*;
import java.awt.event.MouseEvent;
import javax.annotation.Nullable;
import javax.swing.*;

import org.trypticon.talker.swing.ComponentMover;

public class ConnectorLinker extends ComponentMover {
    public static final ConnectorLinker INSTANCE = new ConnectorLinker();

    private NodeView sourceNode;
    private OutputConnectorView sourceConnector;
    private ConnectionView ephemeralConnection;
    private InputConnectorView overConnector;

    private double distanceDrawnSoFar;
    private Point lastLocation;

    private ConnectorLinker() {
    }

    @Override
    public void mouseEntered(MouseEvent event) {
        Object source = event.getSource();
        if (source instanceof InputConnectorView) {
            overConnector = (InputConnectorView) source;
            System.out.println("overConnector now " + overConnector);
        }
    }

    @Override
    public void mouseExited(MouseEvent event) {
        overConnector = null;
    }

    @Nullable
    @Override
    protected Component getDraggedComponent(Component source) {
        InputConnectorView draggedClone;
        if (source instanceof InputConnectorView) {
            ConnectionView existingConnection = ((InputConnectorView) source).getConnection();
            if (existingConnection == null) {
                // User is tugging at an input connector with no connection on it. Why?
                return null;
            }

            // User is pulling out a cable at an input connector.
            // They might plug it in somewhere else, or they might just let it go.

            sourceConnector = existingConnection.getSource();
            draggedClone = ((InputConnectorView) source).detachForDrag();

        } else if (source instanceof OutputConnectorView) {
            sourceConnector = (OutputConnectorView) source;
            draggedClone = sourceConnector.createCloneForDrag();
        } else {
            return null;
        }

        sourceNode = (NodeView) SwingUtilities.getAncestorOfClass(NodeView.class, source);

        distanceDrawnSoFar = 1.0;
        ephemeralConnection = sourceConnector.connectTo(draggedClone, distanceDrawnSoFar);
        lastLocation = draggedClone.getLocation();
        return draggedClone;
    }

    @Override
    protected void handleDraggedComponent(Component draggedClone) {
        Point newLocation = draggedClone.getLocation();
        distanceDrawnSoFar += lastLocation.distance(newLocation);
        ephemeralConnection.setCableLength(distanceDrawnSoFar);
        lastLocation = newLocation;

        if (sourceConnector != null && overConnector != null &&
                sourceConnector.canConnectTo(overConnector)) {

            // TODO: Indicate somehow that we're over a valid target
        }
    }

    @Override
    protected void handleDroppedComponent(Component draggedClone) {
        // This call is here instead of closer to the scope it's used because it might
        // become undefined once you remove it from the panel.
        Rectangle oldBounds = draggedClone.getBounds();

        // You'd think Swing would repaint the component when a child is removed, but nope.
        Container dragPanel = draggedClone.getParent();
        dragPanel.remove(draggedClone);

        // Repaint region is the union rectangle of the source plus the destination,
        // extended downwards enough to encompass the entire cable.
        Rectangle repaintBounds = sourceNode.getBounds();
        repaintBounds.add(oldBounds);
        repaintBounds.height += distanceDrawnSoFar;
        dragPanel.repaint(repaintBounds.x, repaintBounds.y, repaintBounds.width, repaintBounds.height);

        if (sourceConnector != null && overConnector != null) {
            sourceConnector.tryToConnectTo(overConnector, distanceDrawnSoFar);
        }
    }
}
