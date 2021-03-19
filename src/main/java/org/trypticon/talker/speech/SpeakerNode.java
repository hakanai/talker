package org.trypticon.talker.speech;

import com.google.common.collect.ImmutableList;
import org.trypticon.talker.model.ConnectorType;
import org.trypticon.talker.model.GraphLocation;
import org.trypticon.talker.model.InputConnector;
import org.trypticon.talker.model.Node;
import org.trypticon.talker.text.Text;

public abstract class SpeakerNode extends Node {
    protected SpeakerNode(GraphLocation graphLocation, String providerId, String name) {
        super(graphLocation, providerId, name,
                ImmutableList.of(new InputConnector("analyzedText", "Analyzed Text", ConnectorType.TEXT)),
                ImmutableList.of());
    }

    @Override
    public void push(InputConnector connector, Object data) {
        speak((Text) data);
    }

    /**
     * Speaks some text. It's supposed to block until done doing so.
     *
     * @param text the text.
     */
    protected abstract void speak(Text text);
}
