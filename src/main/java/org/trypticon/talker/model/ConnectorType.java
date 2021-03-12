package org.trypticon.talker.model;

import java.awt.*;

public enum ConnectorType {

    /**
     * Plain text message.
     */
    MESSAGE(Color.GREEN),

    /**
     * Analyzed text.
     */
    TEXT(Color.BLUE),

    /**
     * Audio data.
     */
    AUDIO(Color.RED);

    private final Color color;

    ConnectorType(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
