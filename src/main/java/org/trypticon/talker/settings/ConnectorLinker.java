package org.trypticon.talker.settings;

import java.awt.*;
import java.awt.event.MouseEvent;

import javax.swing.*;

import org.trypticon.talker.swing.ComponentMover;

public class ConnectorLinker extends ComponentMover {
    public static final ConnectorLinker INSTANCE = new ConnectorLinker();

    private NodeView sourceNode;
    private ConnectorView sourceConnector;
    private ConnectionView ephemeralConnection;
    private ConnectorView overConnector;

    private double distanceDrawnSoFar;
    private Point lastLocation;

    private ConnectorLinker() {
    }

    @Override
    public void mouseEntered(MouseEvent event) {
        overConnector = (ConnectorView) event.getSource();
        System.out.println("overConnector now " + overConnector);
    }

    @Override
    public void mouseExited(MouseEvent event) {
        overConnector = null;
        System.out.println("overConnector now null");
    }

    @Override
    protected Component getDraggedComponent(Component source) {
        sourceConnector = (ConnectorView) source;
        sourceNode = (NodeView) SwingUtilities.getAncestorOfClass(NodeView.class, source);
        ConnectorView draggedClone = sourceConnector.createCloneForDrag();

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

            // TODO: Indicate this somehow
            System.out.println("currently over a valid target");
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

        if (sourceConnector != null && overConnector != null &&
                sourceConnector.canConnectTo(overConnector)) {

            sourceConnector.connectTo(overConnector, distanceDrawnSoFar);
        }
    }
}
