package org.trypticon.talker.settings;

import java.awt.*;
import java.util.List;
import javax.swing.*;

import com.google.common.collect.ImmutableList;
import org.trypticon.talker.swing.ComponentMover;

public class Node extends JPanel {
    private final SettingsGraph graph;
    private final JLabel titleLabel;
    private final JPanel inputConnectorsPanel = createInputConnectorsPanel();
    private final JPanel mainPanel = createMainPanel();
    private final JPanel outputConnectorsPanel = createOutputConnectorsPanel();
    private final List<Connector> inputConnectors;
    private final List<Connector> outputConnectors;

    public Node(SettingsGraph graph, String title, Point initialLocation, List<Connector> inputConnectors, List<Connector> outputConnectors) {
        this.graph = graph;
        setLocation(initialLocation);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setLayout(new BorderLayout());
        titleLabel = new TitleLabel(title);
        add(titleLabel, BorderLayout.PAGE_START);
        add(inputConnectorsPanel, BorderLayout.LINE_START);
        add(mainPanel, BorderLayout.CENTER);
        add(outputConnectorsPanel, BorderLayout.LINE_END);

        this.inputConnectors = ImmutableList.copyOf(inputConnectors);
        this.outputConnectors = ImmutableList.copyOf(outputConnectors);

        inputConnectors.forEach(inputConnectorsPanel::add);
        outputConnectors.forEach(outputConnectorsPanel::add);

        addMouseListener(new ComponentMover());

        inputConnectors.forEach(c -> c.addMouseListener(ConnectorLinker.INSTANCE));
        outputConnectors.forEach(c -> c.addMouseListener(ConnectorLinker.INSTANCE));
    }

    public List<Connector> getInputConnectors() {
        return inputConnectors;
    }

    public List<Connector> getOutputConnectors() {
        return outputConnectors;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        return panel;
    }

    private JPanel createInputConnectorsPanel() {
        JPanel panel = new JPanel();
        panel.setName("inputConnectorsPanel");
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        return panel;
    }

    private JPanel createOutputConnectorsPanel() {
        JPanel panel = new JPanel();
        panel.setName("outputConnectorsPanel");
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        return panel;
    }


}
