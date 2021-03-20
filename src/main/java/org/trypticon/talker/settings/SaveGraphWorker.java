package org.trypticon.talker.settings;

import java.io.IOException;
import java.nio.file.Paths;
import javax.swing.*;

import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.config.GraphMarshalling;
import org.trypticon.talker.model.Graph;

/**
 * Background worker to save the graph to its file.
 */
public class SaveGraphWorker extends SwingWorker<Void, Void> {
    private final Graph graph;

    public SaveGraphWorker(Graph graph) {
        this.graph = graph;
    }

    @Override
    protected Void doInBackground() throws IOException {
        Configuration configuration = GraphMarshalling.storeGraph(graph);

        configuration.writeToFile(Paths.get("config.json"));

        return null; // void
    }
}
