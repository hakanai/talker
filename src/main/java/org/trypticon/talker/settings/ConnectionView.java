package org.trypticon.talker.settings;

import java.awt.*;
import javax.swing.event.AncestorEvent;

import org.trypticon.talker.swing.AncestorAdapter;
import org.trypticon.talker.util.Colors;

/**
 * A connection between two connectors.
 */
public class ConnectionView {
    private final SettingsGraph graph;
    private final ConnectorView source;
    private final ConnectorView target;
    private double cableLength;
    private final BasicStroke stroke;
    private final Paint paint;
    private final Catenary catenary;

    public ConnectionView(SettingsGraph graph, ConnectorView source, ConnectorView target, double cableLength) {
        this.graph = graph;
        this.source = source;
        this.target = target;
        this.cableLength = cableLength;

        // Potential optimisation here: We could determine how much of the graph
        // needs repainting.
        AncestorAdapter ancestorListener = new AncestorAdapter() {
            @Override
            public void ancestorMoved(AncestorEvent event) {
                updateCatenary();
                graph.repaint();
            }
        };
        source.addAncestorListener(ancestorListener);
        target.addAncestorListener(ancestorListener);

        stroke = new BasicStroke(source.getWidth(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        paint = Colors.desaturate(source.getBackground());
        catenary = new Catenary();
    }

    public ConnectorView getSource() {
        return source;
    }

    public ConnectorView getTarget() {
        return target;
    }

    public double getCableLength() {
        return cableLength;
    }

    public void setCableLength(double cableLength) {
        this.cableLength = cableLength;
    }

    private void updateCatenary() {
        Point graphLocation = graph.getLocationOnScreen();

        Point start = source.getLocationOnScreen();
        start.translate(source.getWidth() / 2, source.getHeight() / 2);
        start.translate(-graphLocation.x, -graphLocation.y);
        Point end = target.getLocationOnScreen();
        end.translate(target.getWidth() / 2, target.getHeight() / 2);
        end.translate(-graphLocation.x, -graphLocation.y);

        catenary.update(start, end, cableLength);
    }

    /**
     * Paints parts of the connection on top of which the nodes will be rendered.
     *
     * @param g the graphics context for the entire graph.
     */
    protected void paintUnder(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(stroke);
        g2d.setPaint(paint);
        catenary.drawToCanvas(g2d);
    }

    // TODO: paintOver I think requires me to customise SettingsGraph.paint IIRC ? Dig.

//    protected void paintOver(Graphics g) {
//        Point start = source.getLocation();
//        Point end = target.getLocation();
//
//        Graphics2D g2d = (Graphics2D) g;
//        g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
//        g2d.drawLine(start.x, start.y, end.x, end.y);
//    }
}
