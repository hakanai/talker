package org.trypticon.talker.settings;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class SelectAction extends AbstractAction {
    private final GraphView graphView;

    public SelectAction(GraphView graphView) {
        this.graphView = graphView;
        putValue(NAME, "Select");
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        graphView.setCursor(Cursor.getDefaultCursor());
    }
}
