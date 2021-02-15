package org.trypticon.talker;

import javax.swing.*;

/**
 * Main entry point.
 */
public class TalkerMain implements Runnable {

    @Override
    public void run() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // no problem, use the default which has already been set
        }

        new TalkerFrame();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new TalkerMain());
    }
}
