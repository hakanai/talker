package org.trypticon.talker.settings;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;

import org.trypticon.talker.model.Node;
import org.trypticon.talker.swing.ComponentMover;
import org.trypticon.talker.swing.RoundRectBorder;

public class NodeView extends JPanel {
    private final GraphView graph;
    private final JLabel titleLabel;
    private final JPanel inputConnectorsPanel = createInputConnectorsPanel();
    private final JPanel mainPanel = createMainPanel();
    private final JPanel outputConnectorsPanel = createOutputConnectorsPanel();
    private final List<InputConnectorView> inputConnectors;
    private final List<OutputConnectorView> outputConnectors;

    public NodeView(GraphView graphView, Node node) {
        this.graph = graphView;

        setLocation(node.getX(), node.getY());
        setOpaque(false);
        setBorder(new RoundRectBorder());
        setLayout(new BorderLayout());
        titleLabel = new TitleLabel(node.getName());

        JPanel titledPanel = new JPanel(new BorderLayout());
        titledPanel.setOpaque(false);
        titledPanel.add(titleLabel, BorderLayout.PAGE_START);
        titledPanel.add(mainPanel, BorderLayout.CENTER);

        add(inputConnectorsPanel, BorderLayout.PAGE_START);
        add(titledPanel, BorderLayout.CENTER);
        add(outputConnectorsPanel, BorderLayout.PAGE_END);

        this.inputConnectors = node.getInputConnectors().stream()
                .map(c -> new InputConnectorView(graphView, c))
                .collect(Collectors.toUnmodifiableList());
        this.outputConnectors = node.getOutputConnectors().stream()
                .map(c -> new OutputConnectorView(graphView, c))
                .collect(Collectors.toUnmodifiableList());

        inputConnectors.forEach(inputConnectorsPanel::add);
        outputConnectors.forEach(outputConnectorsPanel::add);

        addMouseListener(new ComponentMover());

        inputConnectors.forEach(c -> c.addMouseListener(ConnectorLinker.INSTANCE));
        outputConnectors.forEach(c -> c.addMouseListener(ConnectorLinker.INSTANCE));
    }

    public List<InputConnectorView> getInputConnectors() {
        return inputConnectors;
    }

    public List<OutputConnectorView> getOutputConnectors() {
        return outputConnectors;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());
        return panel;
    }

    private JPanel createInputConnectorsPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setName("inputConnectorsPanel");
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        return panel;
    }

    private JPanel createOutputConnectorsPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setName("outputConnectorsPanel");
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        return panel;
    }


}
