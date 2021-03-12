package org.trypticon.talker.settings;

import java.awt.*;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.*;

import org.trypticon.talker.swing.DragLayout;

public class SettingsGraph extends JComponent {

    private final List<ConnectionView> connections = new ArrayList<>();

    public SettingsGraph() {
        setLayout(new DragLayout(false));

        //addSampleNodes2();

        addContainerListener(new ContainerAdapter() {
            @Override
            public void componentRemoved(ContainerEvent event) {
                Component child = event.getChild();
                if (child instanceof ConnectorView) {
                    removeAll(getConnectionsTo((ConnectorView) child)
                            .collect(Collectors.toUnmodifiableList()));
                }
            }
        });
    }

    public void add(ConnectionView connection) {
        connections.add(connection);
    }

    public void remove(ConnectionView connection) {
        connections.remove(connection);
    }

    public void removeAll(Collection<? extends ConnectionView> connections) {
        this.connections.removeAll(connections);
    }

    public Stream<ConnectionView> getConnectionsTo(ConnectorView connector) {
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
