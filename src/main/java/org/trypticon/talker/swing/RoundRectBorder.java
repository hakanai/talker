package org.trypticon.talker.swing;

import java.awt.*;
import javax.swing.border.Border;

/**
 * A border which renders a roundrect.
 */
public class RoundRectBorder implements Border {
    private final float radius;
    private final float strokeWidth;
    private final Color borderColor = Color.BLACK;

    public RoundRectBorder() {
        this(15.0f);
    }

    public RoundRectBorder(float radius) {
        this(radius, 1.0f);
    }

    public RoundRectBorder(float radius, float strokeWidth) {
        this.radius = radius;
        this.strokeWidth = strokeWidth;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        try {
            g2d.translate(x, y);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(c.getBackground());
            g2d.fillRoundRect(0, 0, width - (int) strokeWidth, height - (int) strokeWidth, (int) radius, (int) radius);
            g2d.setPaint(borderColor);
            g2d.setStroke(new BasicStroke(strokeWidth));
            g2d.drawRoundRect(0, 0, width - (int) strokeWidth, height - (int) strokeWidth, (int) radius, (int) radius);
        } finally {
            g2d.dispose();
        }
    }

    @Override
    public Insets getBorderInsets(Component c) {
        int floor = (int) Math.floor(strokeWidth / 2.0);
        int ceil = (int) Math.ceil(strokeWidth / 2.0);
        return new Insets(floor, floor, ceil, ceil);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}
