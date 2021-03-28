package org.trypticon.talker.text.tokenisation;

import com.google.common.collect.ImmutableList;
import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.messages.Message;
import org.trypticon.talker.model.*;
import org.trypticon.talker.text.Text;

public class KuromojiTokenizerNode extends Node {
    KuromojiTokenizer tokenizer = new KuromojiTokenizer();

    private final OutputConnector analyzedTextConnector;

    public KuromojiTokenizerNode(GraphLocation graphLocation, String providerId) {
        super(graphLocation, providerId, "Kuromoji Tokenizer",
                ImmutableList.of(new InputConnector("messages", "Messages", ConnectorType.MESSAGE)),
                ImmutableList.of(new OutputConnector("analyzedText", "Analyzed Text", ConnectorType.TEXT)));

        analyzedTextConnector = getOutputConnectors().get(0);
    }

    @Override
    public void populateConfiguration(Configuration.Builder builder) {
    }

    @Override
    public void push(InputConnector connector, Object data) {
        Message message = (Message) data;
        Text text = tokenizer.tokenize(message.getText());
        sendOutput(analyzedTextConnector, text);
    }
}
