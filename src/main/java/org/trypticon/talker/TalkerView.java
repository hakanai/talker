package org.trypticon.talker;

import java.util.Locale;

/**
 * View interface called from the presenter.
 */
public interface TalkerView {

    Locale getLocale();

    void appendMarkup(String markup);

}
