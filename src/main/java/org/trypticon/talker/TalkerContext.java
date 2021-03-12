package org.trypticon.talker;

/**
 * Context object allowing components to access various parts of the system without
 * factories having to know about every part of the system.
 */
public class TalkerContext {
    private final TalkerView view;

    public TalkerContext(TalkerView view) {
        this.view = view;
    }

    public TalkerView getView() {
        return view;
    }
}
