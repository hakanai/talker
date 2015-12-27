package org.trypticon.talker;

import javax.swing.*;

/**
 * Main entry point.
 */
public class TalkerMain implements Runnable {

    @Override
    public void run() {
        new TalkerFrame();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new TalkerMain());
    }
}
