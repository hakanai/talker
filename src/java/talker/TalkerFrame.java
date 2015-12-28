package talker;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.HTMLDocument;
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

        editorPane = new JTextPane();
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");
        appendMarkupOnly("<div id=\"bottom\">&nbsp;</div>");

        statusLabel = new JLabel(" ");
        statusLabel.putClientProperty( "JComponent.sizeVariant", "mini" );

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        bottomPanel.add(statusLabel);

        setLayout(new BorderLayout());
        add(new JScrollPane(editorPane), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.PAGE_END);

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
    public void appendMarkup(String markup) {
        appendMarkupOnly(markup);
        editorPane.scrollToReference("bottom");
    }

    private void appendMarkupOnly(String markup) {
        HTMLDocument document = (HTMLDocument) editorPane.getDocument();
        Element root = document.getDefaultRootElement();
        Element bottom = root.getElement(root.getElementCount() - 1);
        try {
            document.insertBeforeStart(bottom, markup);
        } catch (BadLocationException | IOException e) {
            throw new IllegalStateException("BadLocationException for a location given to us", e);
        }
    }

    @Override
    public void updateStatus(String message) {
        statusLabel.setText(message);
    }
}
