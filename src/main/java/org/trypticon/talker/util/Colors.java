package org.trypticon.talker.util;

import java.awt.*;

public class Colors {
    private Colors() {}

    public static Color desaturate(Color input) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(input.getRed(), input.getGreen(), input.getBlue(), hsb);
        hsb[1] *= 0.5f;
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }
}
