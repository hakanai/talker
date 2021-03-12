package org.trypticon.talker.text.tokenisation;

import com.google.common.collect.ImmutableList;
import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.messages.Message;
import org.trypticon.talker.model.*;
import org.trypticon.talker.text.Text;

public class KuromojiTokenizerNode extends Node {
    KuromojiTokenizer tokenizer = new KuromojiTokenizer();

    private final OutputConnector analyzedTextConnector;

    public KuromojiTokenizerNode(Graph graph, Configuration configuration) {
        super("Kuromoji Tokenizer",
                ImmutableList.of(new InputConnector("messages", "Messages", ConnectorType.MESSAGE)),
                ImmutableList.of(new OutputConnector("analyzedText", "Analyzed Text", ConnectorType.TEXT, graph)));

        analyzedTextConnector = getOutputConnectors().get(0);
    }

    @Override
    public void push(InputConnector connector, Object data) {
        Message message = (Message) data;
        Text text = tokenizer.tokenise(message.getText());
        sendOutput(analyzedTextConnector, text);
    }
}
