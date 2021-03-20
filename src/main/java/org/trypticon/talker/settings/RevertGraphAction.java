package org.trypticon.talker.settings;

import java.awt.event.ActionEvent;
import javax.swing.*;

public class RevertGraphAction extends AbstractAction {
    private final GraphView graphView;

    public RevertGraphAction(GraphView graphView) {
        this.graphView = graphView;

        putValue(NAME, "Revert to Saved Graph"); // TODO: Localise
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        graphView.loadGraphFromFile();
    }
}
