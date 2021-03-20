package org.trypticon.talker.settings;

import java.awt.*;
import javax.swing.*;

public class SettingsFrame extends JFrame {
    public SettingsFrame() {
        super("Talker Settings");

        setLayout(new BorderLayout());

        SettingsToolBar toolBar = new SettingsToolBar();
        add(toolBar, BorderLayout.LINE_START);

        GraphView graphView = new GraphView();
        graphView.loadGraphFromFile();

        add(graphView, BorderLayout.CENTER);

        setJMenuBar(buildMenuBar(graphView));
    }

    private JMenuBar buildMenuBar(GraphView graphView) {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File"); // TODO: Localisation
        fileMenu.add(new RevertGraphAction(graphView));
        fileMenu.add(new SaveGraphAction(graphView));
        menuBar.add(fileMenu);

        return menuBar;
    }
}
