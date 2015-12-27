package org.trypticon.talker;

/**
 * View interface called from the presenter.
 */
public interface TalkerView {

    void appendMarkup(String markup);

    void updateStatus(String message);
}
