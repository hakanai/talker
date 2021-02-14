package org.trypticon.talker;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * Main application frame.
 */
public class TalkerFrame extends JFrame implements TalkerView {
    private final TalkerPresenter presenter;
    private final JEditorPane editorPane;
    private final JLabel statusLabel;

    public TalkerFrame() {
        super("Talker");

        editorPane = new CustomTextPane();

        statusLabel = new JLabel(" ");
        statusLabel.putClientProperty("JComponent.sizeVariant", "mini");

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        bottomPanel.add(statusLabel);

        setLayout(new BorderLayout());
        add(new JScrollPane(editorPane), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.PAGE_END);

        pack();
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
    public void appendMarkup(String markup) {
        HTMLDocument document = (HTMLDocument) editorPane.getDocument();
        Element bottom = document.getElement("bottom");
        try {
            document.insertBeforeStart(bottom, markup);
        } catch (BadLocationException | IOException e) {
            throw new IllegalStateException("BadLocationException for a location given to us", e);
        }

        editorPane.scrollToReference("bottom");
    }

    @Override
    public void updateStatus(String message) {
        statusLabel.setText(message);
    }

    private static class CustomTextPane extends JTextPane {
        private CustomTextPane() {
            setEditable(false);
            setContentType("text/html");
            putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
            updateUI();
            setText("<html>\n" +
                    "<body>\n" +
                    "<div id=\"bottom\">&nbsp;</div>\n" +
                    "</body>\n" +
                    "</html>");
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            FontMetrics metrics = getFontMetrics(getFont());
            return new Dimension(
                    metrics.charWidth('m') * 50,
                    metrics.getHeight() * 20);
        }
    }
}
