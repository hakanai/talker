package org.trypticon.talker.settings;

import java.awt.event.ActionEvent;
import javax.swing.*;

public class NewNodeAction extends AbstractAction {
    private final SettingsGraph graph;
    private final String name;
    private final org.trypticon.talker.model.NodeFactory nodeFactory;

    public NewNodeAction(SettingsGraph graph, String name, org.trypticon.talker.model.NodeFactory nodeFactory) {
        this.graph = graph;
        this.name = name;
        this.nodeFactory = nodeFactory;

        putValue(NAME, "New " + name);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO: How do you configure the node?
        // nodeFactory.create()
    }
}
