package org.trypticon.talker;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Main application frame.
 */
public class TalkerFrame extends JFrame implements TalkerView {
    private final TalkerPresenter presenter;
    private final JTextPane textPane;

    public TalkerFrame() {
        super("Talker");

        textPane = new JTextPane();
        textPane.setEditable(false);

        setLayout(new BorderLayout());
        add(new JScrollPane(textPane), BorderLayout.CENTER);

        setSize(500, 350);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        presenter = new TalkerPresenter(this);
        presenter.start();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent event) {
                presenter.stop();
            }
        });
    }

    @Override
    public void appendText(String text) {
        try {
            Document document = textPane.getDocument();
            //TODO: Attributes
            document.insertString(document.getLength(), text, null);
            textPane.setCaretPosition(document.getLength());
        } catch (BadLocationException e) {
            throw new IllegalStateException("BadLocationException for a location given to us", e);
        }
    }
}
