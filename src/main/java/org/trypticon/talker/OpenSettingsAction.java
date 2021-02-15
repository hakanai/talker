package org.trypticon.talker;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;

import org.trypticon.talker.settings.SettingsFrame;

public class OpenSettingsAction extends AbstractAction {
    private final TalkerFrame mainFrame;

    public OpenSettingsAction(TalkerFrame mainFrame) {
        this.mainFrame = mainFrame;

        putValue(NAME, "Open Settings");
        putValue(MNEMONIC_KEY, KeyEvent.VK_S);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        // Maybe it's already present?
        for (Frame frame : Frame.getFrames()) {
            if (frame instanceof SettingsFrame && frame.isVisible()) {
                frame.requestFocusInWindow();
                return;
            }
        }

        SettingsFrame settingsFrame = new SettingsFrame();
        settingsFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        settingsFrame.setSize(mainFrame.getSize());
        settingsFrame.setVisible(true);
    }
}
