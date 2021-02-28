package org.trypticon.talker.settings;

import java.awt.*;

public enum ConnectorType {
    TEXT(Color.BLUE),

    AUDIO(Color.RED);

    private final Color color;

    ConnectorType(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
