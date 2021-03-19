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

        new LoadGraphWorker()
                .whenDone(graphView::setGraph)
                .execute();

        add(graphView, BorderLayout.CENTER);
    }
}
