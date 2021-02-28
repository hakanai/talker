package org.trypticon.talker.settings;

import java.awt.*;
import javax.swing.*;

public class TitleLabel extends JLabel {
    public TitleLabel(String title) {
        setText(title);
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    }

    @Override
    public Font getFont() {
        Font font = super.getFont();
        if (font == null) {
            return null;
        }
        return font.deriveFont(Font.BOLD);
    }
}
