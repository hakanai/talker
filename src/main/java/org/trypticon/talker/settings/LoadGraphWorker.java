package org.trypticon.talker.settings;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import javax.swing.*;

import org.trypticon.talker.TalkerContext;
import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.config.GraphMarshalling;
import org.trypticon.talker.model.Graph;

/**
 * Background worker to load the graph from its file.
 */
public class LoadGraphWorker extends SwingWorker<Graph, Void> {
    private Consumer<Graph> whenDone;

    @Override
    protected Graph doInBackground() throws IOException {
        Configuration configuration = Configuration.readFromFile(Paths.get("config.json"));
        TalkerContext context = new TalkerContext(null);
        return GraphMarshalling.loadGraph(context, configuration);
    }

    public LoadGraphWorker whenDone(Consumer<Graph> runner) {
        whenDone = runner;
        return this;
    }

    @Override
    protected void done() {
        Graph graph = null;
        try {
            graph = get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        whenDone.accept(graph);
    }
}
