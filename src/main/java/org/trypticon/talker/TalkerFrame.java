package org.trypticon.talker;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;

/**
 * Main application frame.
 */
public class TalkerFrame extends JFrame implements TalkerView {
    private final TalkerPresenter presenter;
    private final JEditorPane editorPane;
    private final JLabel statusLabel;

    public TalkerFrame() {
        super("Talker");

        Desktop desktop = Desktop.getDesktop();

        JMenuBar menuBar = buildMenuBar(this);
        setJMenuBar(menuBar);
        if (desktop != null && desktop.isSupported(Desktop.Action.APP_MENU_BAR)) {
            desktop.setDefaultMenuBar(menuBar);
        }

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

    private static JMenuBar buildMenuBar(TalkerFrame mainFrame) {
        JMenuBar menuBar = new JMenuBar();
        Desktop desktop = Desktop.getDesktop();

        // TODO: This is very skeleton and users expect a lot more to be in the menu.
        JMenu fileMenu = new JMenu("File");

        fileMenu.add(new OpenSettingsAction(mainFrame));

        // Give people on OSes with no proper exit behaviour an action to do the job.
        if (!desktop.isSupported(Desktop.Action.APP_QUIT_HANDLER)) {
            fileMenu.add(new ExitAction(mainFrame));
        }

        menuBar.add(fileMenu);
        return menuBar;
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
                    metrics.charWidth('m') * 80,
                    metrics.getHeight() * 40);
        }
    }
}
