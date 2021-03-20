package org.trypticon.talker.settings;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.AncestorEvent;

import org.trypticon.talker.swing.AncestorAdapter;

/**
 * A connection between two connectors.
 */
public class ConnectionView {
    private final GraphView graph;
    private final OutputConnectorView source;
    private final InputConnectorView target;
    private final BasicStroke stroke;
    private final Paint paint;
    private final Catenary catenary;

    private double cableLength;

    public ConnectionView(GraphView graph, OutputConnectorView source, InputConnectorView target, double cableLength) {
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

        stroke = new BasicStroke(source.getPreferredSize().width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        Color c = source.getBackground();
        paint = new Color(c.getRed(), c.getGreen(), c.getBlue(), 128);
        catenary = new Catenary();
    }

    public OutputConnectorView getSource() {
        return source;
    }

    public InputConnectorView getTarget() {
        return target;
    }

    public double getCableLength() {
        return cableLength;
    }

    public void setCableLength(double cableLength) {
        this.cableLength = cableLength;
    }

    private void updateCatenary() {
        Point start = SwingUtilities.convertPoint(source, source.getWidth() / 2, source.getHeight() / 2, graph);
        Point end = SwingUtilities.convertPoint(target, target.getWidth() / 2, target.getHeight() / 2, graph);
        catenary.update(start, end, cableLength);
    }

    /**
     * Paints parts of the connection on top of which the nodes will be rendered.
     *
     * @param g the graphics context for the entire graph.
     */
    protected void paintUnder(Graphics2D g) {
        g.setStroke(stroke);
        g.setPaint(paint);
        catenary.drawToCanvas(g);
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
