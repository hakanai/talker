package org.trypticon.talker.settings;

import java.awt.event.ActionEvent;
import javax.swing.*;

public class SaveGraphAction extends AbstractAction {
    private final GraphView graphView;

    public SaveGraphAction(GraphView graphView) {
        this.graphView = graphView;

        putValue(NAME, "Save Settings Graph"); // TODO: Localise
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        graphView.saveGraphToFile();
    }
}
