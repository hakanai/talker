package org.trypticon.talker.settings;

import javax.swing.*;

public class SettingsToolBar extends JToolBar {
    public SettingsToolBar() {
        super("Tools", JToolBar.VERTICAL);

        add(new JToggleButton("Select"));
        addSeparator();

        // TODO: How to get the list of all factories and provide UI for creating their nodes?
        // add(new JToggleButton(new NewNodeAction(graph, "UStream Messages", UStreamMessagesNode::new)));

    }
}
