package org.trypticon.talker;

import org.trypticon.talker.model.Graph;

/**
 * Context object allowing components to access various parts of the system without
 * factories having to know about every part of the system.
 */
public class TalkerContext {
    private final TalkerView view;
    private Graph currentGraph;

    public TalkerContext(TalkerView view) {
        this.view = view;
    }

    public TalkerView getView() {
        return view;
    }

    public Graph getCurrentGraph() {
        return currentGraph;
    }

    public void setCurrentGraph(Graph currentGraph) {
        this.currentGraph = currentGraph;
    }
}
