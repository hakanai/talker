package org.trypticon.talker;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class ExitAction extends AbstractAction {
    private final TalkerFrame mainFrame;

    public ExitAction(TalkerFrame mainFrame) {
        this.mainFrame = mainFrame;

        putValue(NAME, "Exit");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        mainFrame.dispose();
    }
}
