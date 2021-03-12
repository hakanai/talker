package org.trypticon.talker.speech;

import com.google.common.collect.ImmutableList;
import org.trypticon.talker.model.ConnectorType;
import org.trypticon.talker.model.InputConnector;
import org.trypticon.talker.model.Node;
import org.trypticon.talker.text.Text;

public class SpeakerNode extends Node {
    private final Speaker speaker;

    public SpeakerNode(String name, Speaker speaker) {
        super(name,
                ImmutableList.of(new InputConnector("analyzedText", "Analyzed Text", ConnectorType.TEXT)),
                ImmutableList.of());
        this.speaker = speaker;
    }

    @Override
    public void push(InputConnector connector, Object data) {
        speaker.speak((Text) data);
    }
}
