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

    private final List<Connection> connections = new ArrayList<>();

    public SettingsGraph() {
        setLayout(new DragLayout(false));

        addSampleNodes2();

        addContainerListener(new ContainerAdapter() {
            @Override
            public void componentRemoved(ContainerEvent event) {
                Component child = event.getChild();
                if (child instanceof Connector) {
                    removeAll(getConnectionsTo((Connector) child)
                            .collect(Collectors.toUnmodifiableList()));
                }
            }
        });
    }

    public NodeBuilder nodeBuilder(String title) {
        return new NodeBuilder(this, title);
    }

    public void add(Connection connection) {
        connections.add(connection);
    }

    public void remove(Connection connection) {
        connections.remove(connection);
    }

    public void removeAll(Collection<? extends Connection> connections) {
        this.connections.removeAll(connections);
    }

    public Stream<Connection> getConnectionsTo(Connector connector) {
        return connections.stream()
                .filter(connection -> connection.getSource() == connector ||
                        connection.getTarget() == connector);
    }

    private void addSampleNodes1() {

        add(nodeBuilder("Twitch Chat")
                .initialLocation(50, 50)
                .outputConnector(ConnectorType.TEXT)
                .build());

        add(nodeBuilder("Text Macros")
                .initialLocation(100, 100)
                .inputConnector(ConnectorType.TEXT)
                .outputConnector(ConnectorType.TEXT)
                .build());

        add(nodeBuilder("Filter Text")
                .initialLocation(150, 150)
                .inputConnector(ConnectorType.TEXT)
                .outputConnector(ConnectorType.TEXT)
                .build());

        add(nodeBuilder("MaryTTS")
                .initialLocation(200, 200)
                .inputConnector(ConnectorType.TEXT)
                .outputConnector(ConnectorType.AUDIO)
                .build());

        add(nodeBuilder("Audio Player")
                .initialLocation(250, 250)
                .inputConnector(ConnectorType.AUDIO)
                .build());
    }

    private void addSampleNodes2() {

        add(nodeBuilder("Microphone")
                .initialLocation(50, 50)
                .outputConnector(ConnectorType.AUDIO)
                .build());

        add(nodeBuilder("Speech Recognition")
                .initialLocation(100, 100)
                .inputConnector(ConnectorType.AUDIO)
                .outputConnector(ConnectorType.TEXT)
                .build());

        add(nodeBuilder("Filter Text")
                .initialLocation(150, 150)
                .inputConnector(ConnectorType.TEXT)
                .outputConnector(ConnectorType.TEXT)
                .build());

        add(nodeBuilder("Text-to-Speech")
                .initialLocation(200, 200)
                .inputConnector(ConnectorType.TEXT)
                .outputConnector(ConnectorType.AUDIO)
                .build());

        add(nodeBuilder("Audio Player")
                .initialLocation(250, 250)
                .inputConnector(ConnectorType.AUDIO)
                .build());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // TODO: Paint background pattern here

        for (Connection connection : connections) {
            connection.paintUnder(g);
        }

        // Nodes themselves are child components so they will be painted for us.
    }
}
