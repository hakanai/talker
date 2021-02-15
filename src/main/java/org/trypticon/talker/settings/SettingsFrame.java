package org.trypticon.talker.settings;

import java.awt.*;
import javax.swing.*;

public class SettingsFrame extends JFrame {
    public SettingsFrame() {
        super("Talker Settings");

        setLayout(new BorderLayout());

        JToolBar toolBar = new JToolBar("Tools", JToolBar.VERTICAL);
        toolBar.add(new JToggleButton("Select"));
        add(toolBar, BorderLayout.LINE_START);

        SettingsGraph graph = new SettingsGraph();
        add(graph, BorderLayout.CENTER);
    }
}
