package org.trypticon.talker.settings;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Paths;
import javax.swing.*;

import org.trypticon.talker.TalkerContext;
import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.config.GraphMarshalling;
import org.trypticon.talker.model.Graph;

public class SettingsFrame extends JFrame {
    public SettingsFrame() {
        super("Talker Settings");

        setLayout(new BorderLayout());

        SettingsToolBar toolBar = new SettingsToolBar();
        add(toolBar, BorderLayout.LINE_START);

        // TODO: Some awkward code repetition here.
        Configuration configuration;
        try {
            configuration = Configuration.readFromFile(Paths.get("config.json"));
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't load config.json", e);
        }

        TalkerContext context = new TalkerContext(null);
        Graph graph = GraphMarshalling.loadGraph(context, configuration);

        GraphView graphView = new GraphView(graph);
        add(graphView, BorderLayout.CENTER);
    }
}
