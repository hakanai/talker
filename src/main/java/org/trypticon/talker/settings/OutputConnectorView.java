package org.trypticon.talker.settings;

import org.trypticon.talker.model.OutputConnector;

public class OutputConnectorView extends AbstractConnectorView<OutputConnector> {
    public OutputConnectorView(GraphView graph, OutputConnector connector) {
        super(graph, connector);
    }

    public boolean canConnectTo(InputConnectorView overConnector) {
        return getConnector().getType() == overConnector.getConnector().getType() &&
                !overConnector.hasConnection();
    }

    /**
     * Connects to another connector.
     *
     * @param otherConnector the connector we are connecting to.
     * @param cableLength the length of the cable.
     * @return the connection view.
     */
    public ConnectionView connectTo(InputConnectorView otherConnector, double cableLength) {
        GraphView graphView = getGraphView();
        // TODO: What if it's already connected?
        ConnectionView newConnection = new ConnectionView(graphView, this, otherConnector, cableLength);
        graphView.add(newConnection);
        return newConnection;
    }

    /**
     * Convenience method which connects if possible, and otherwise does not.
     *
     * @param otherConnector the connector we are trying to connect to.
     * @param cableLength the length of the cable.
     * @return `true` if the connection actually happened, `false` otherwise.
     */
    public boolean tryToConnectTo(InputConnectorView otherConnector, double cableLength) {
        if (canConnectTo(otherConnector)) {
            connectTo(otherConnector, cableLength);
            return true;
        } else {
            return false;
        }
    }

}
