package org.trypticon.talker;

import java.io.IOException;
import java.nio.file.Paths;

import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.config.GraphMarshalling;
import org.trypticon.talker.model.Graph;

/**
 * Coordinates messages, the display and the speech queue.
 */
class TalkerPresenter {
    private final Configuration configuration;
    private final Graph graph;
    private final TalkerView view;

    TalkerPresenter(TalkerView view) {
        this.view = view;

        try {
            configuration = Configuration.readFromFile(Paths.get("config.json"));
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't load config.json", e);
        }

        TalkerContext context = new TalkerContext(view);
        graph = GraphMarshalling.loadGraph(context, configuration);
        context.setCurrentGraph(graph);
    }

    public void start() {
        graph.start();
    }

    public void stop() {
        graph.stop();
    }
}
