package org.trypticon.talker.swing;

import java.awt.*;

/**
 *  The DragLayout is used as a layout manager for a Container that supports
 *  random dragging of components with the Container.
 *
 *  The location of the component will not be managed by the layout manager.
 *
 * The size of the component can be either the preferred size of the component,
 *  or the actual size of the component.
 *
 *  The preferred size of the container will calculated normally based on the
 *  actual location and size of each component.
 *
 *  A Component cannot have a location outside the bounds of the parent container.
 *  That is the x/y coordinate must be withing the Insets of the Container. If
 *  any component is outside the bounds, then the location of all components
 *  will be adjusted by the same amount.
 */
public class DragLayout implements LayoutManager {
    @Override
    public void addLayoutComponent(String name, Component component) {}

    @Override
    public void removeLayoutComponent(Component component) {}

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            return preferredLayoutSize(parent);
        }
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets parentInsets = parent.getInsets();
            int x = parentInsets.left;
            int y = parentInsets.top;
            int width = 0;
            int height = 0;

            // Get extreme values of the components on the container.
            // The x/y values represent the starting point relative to the
            // top/left of the container. The width/height values represent
            // the bottom/right value within the container.

            Point reusedPoint = new Point();
            Dimension reusedDimension = new Dimension();
            for (Component component: parent.getComponents()) {
                if (component.isVisible()) {
                    Point p = component.getLocation(reusedPoint);
                    Dimension d = getActualSize(component, reusedDimension);
                    x = Math.min(x, p.x);
                    y = Math.min(y, p.y);
                    width = Math.max(width, p.x + d.width);
                    height = Math.max(height, p.y + d.height);
                }
            }

            // Width/Height is adjusted if any component is outside left/top edge
            if (x < parentInsets.left) {
                width += parentInsets.left - x;
            }
            if (y < parentInsets.top) {
                height += parentInsets.top - y;
            }

            //  Adjust for insets
            width += parentInsets.right;
            height += parentInsets.bottom;

            return new Dimension(width, height);
        }
    }

    private Dimension getActualSize(Component component, Dimension reusedDimension) {
        // Use the preferred size as a default when a size has not been set.
        Dimension d = component.getSize(reusedDimension);
        if (d.width == 0 || d.height == 0) {
            return component.getPreferredSize();
        } else {
            return d;
        }
    }

    /**
     * Lays out the specified container using this layout.
     *
     * @param parent the container in which to do the layout
     */
    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets parentInsets = parent.getInsets();

            int x = parentInsets.left;
            int y = parentInsets.top;

            //  Get x/y location of any component outside the bounds of the panel.
            //  All components will be adjust by the x/y values, if necessary.

            for (Component component : parent.getComponents()) {
                if (component.isVisible()) {
                    Point location = component.getLocation();
                    x = Math.min(x, location.x);
                    y = Math.min(y, location.y);
                }
            }

            x = (x < parentInsets.left) ? parentInsets.left - x : 0;
            y = (y < parentInsets.top) ? parentInsets.top - y : 0;

            // Set bounds of each component
            Point reusedPoint = new Point();
            Dimension reusedDimension = new Dimension();
            for (Component component : parent.getComponents()) {
                if (component.isVisible()) {
                    Point p = component.getLocation(reusedPoint);
                    Dimension d = getActualSize(component, reusedDimension);
                    component.setBounds(p.x + x, p.y + y, d.width, d.height);
                }
            }
        }
    }
}