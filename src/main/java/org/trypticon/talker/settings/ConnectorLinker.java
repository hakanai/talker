package org.trypticon.talker.settings;

import java.awt.*;
import java.awt.event.MouseEvent;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;

import org.trypticon.talker.swing.ComponentMover;

public class ConnectorLinker extends ComponentMover {
    public static final ConnectorLinker INSTANCE = new ConnectorLinker();

    private OutputConnectorView sourceConnector;
    private ConnectionView ephemeralConnection;
    private InputConnectorView overConnector;

    private double distanceDrawnSoFar;
    private boolean shouldExtend;
    private Point lastLocation;

    private ConnectorLinker() {
    }

    @Override
    public void mouseEntered(MouseEvent event) {
        Object source = event.getSource();
        if (source instanceof InputConnectorView) {
            overConnector = (InputConnectorView) source;
        }
    }

    @Override
    public void mouseExited(MouseEvent event) {
        overConnector = null;
    }

    @Nullable
    @Override
    protected Component getDraggedComponent(@Nonnull Component source) {
        InputConnectorView draggedClone;
        if (source instanceof InputConnectorView) {
            ConnectionView existingConnection = ((InputConnectorView) source).getConnection();
            if (existingConnection == null) {
                // User is tugging at an input connector with no connection on it. Why?
                return null;
            }

            // User is pulling out a cable at an input connector.
            // They might plug it in somewhere else, or they might just let it go.

            overConnector = (InputConnectorView) source;
            sourceConnector = existingConnection.getSource();
            draggedClone = ((InputConnectorView) source).detachForDrag();
            distanceDrawnSoFar = existingConnection.getCableLength();
            shouldExtend = false;

        } else if (source instanceof OutputConnectorView) {
            sourceConnector = (OutputConnectorView) source;
            draggedClone = sourceConnector.createCloneForDrag();
            distanceDrawnSoFar = 1.0;
            shouldExtend = true;

        } else {
            return null;
        }

        if (draggedClone == null) {
            return null;
        }

        ephemeralConnection = sourceConnector.connectTo(draggedClone, distanceDrawnSoFar);
        lastLocation = draggedClone.getLocation();

        repaintConnection(draggedClone);

        return draggedClone;
    }

    @Override
    protected void handleDraggedComponent(@Nonnull Component draggedClone) {
        repaintConnection(draggedClone);

        Point newLocation = draggedClone.getLocation();
        if (shouldExtend) {
            distanceDrawnSoFar += lastLocation.distance(newLocation);
        }
        ephemeralConnection.setCableLength(distanceDrawnSoFar);
        lastLocation = newLocation;

        if (sourceConnector != null && overConnector != null &&
                sourceConnector.canConnectTo(overConnector)) {

            // TODO: Indicate somehow that we're over a valid target
        }
    }

    @Override
    protected void handleDroppedComponent(@Nonnull Component draggedClone) {
        repaintConnection(draggedClone);

        Container dragPanel = draggedClone.getParent();
        dragPanel.remove(draggedClone);

        if (sourceConnector != null && overConnector != null) {
            sourceConnector.tryToConnectTo(overConnector, distanceDrawnSoFar);
        }
    }

    private void repaintConnection(@Nonnull Component draggedClone) {
        Container dragPanel = draggedClone.getParent();

        // Repaint region is the union rectangle of the source plus the destination,
        // extended downwards enough to encompass the entire cable.
        Rectangle repaintBounds = sourceConnector.getBounds();
        repaintBounds = SwingUtilities.convertRectangle(sourceConnector.getParent(),
                repaintBounds, draggedClone.getParent());
        repaintBounds.add(draggedClone.getBounds());

        // This is an upper bound. I don't know if there is a way to determine a more conservative value.
        repaintBounds.height += distanceDrawnSoFar;

        dragPanel.repaint(repaintBounds.x, repaintBounds.y, repaintBounds.width, repaintBounds.height);
    }
}
