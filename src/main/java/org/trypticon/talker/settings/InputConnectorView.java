package org.trypticon.talker.settings;

import javax.annotation.Nullable;

import org.trypticon.talker.model.InputConnector;

public class InputConnectorView extends AbstractConnectorView<InputConnector> {
    public InputConnectorView(GraphView graphView, InputConnector connector) {
        super(graphView, connector);
    }

    /**
     * Convenience method to test whether a connection is present.
     *
     * @return `true` if a connection is present, `false` otherwise.
     */
    public boolean hasConnection() {
        return getConnections().findAny().isPresent();
    }

    /**
     * Convenience method to get the single connection an input can have.
     *
     * @return the connection, if present, otherwise `null`.
     */
    @Nullable
    public ConnectionView getConnection() {
        return getConnections().findAny().orElse(null);
    }

    /**
     * Detaches this connector from the graph ready for the user to drag it somewhere
     * else (or potentially to nowhere.)
     *
     * @return the draggable connector.
     */
    @Nullable
    public final InputConnectorView detachForDrag() {
        GraphView graphView = getGraphView();

        ConnectionView connection = getConnection();
        if (connection == null) {
            return null;
        }

        graphView.remove(connection);

        return createCloneForDrag();
    }
}
