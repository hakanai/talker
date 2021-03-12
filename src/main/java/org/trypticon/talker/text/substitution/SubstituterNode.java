package org.trypticon.talker.text.substitution;

import com.google.common.collect.ImmutableList;
import org.trypticon.talker.model.*;
import org.trypticon.talker.text.Text;

/**
 * Base class for nodes which convert analysed text to analysed text,
 * potentially substituting some of the text.
 */
public abstract class SubstituterNode extends Node implements Substituter {
    private final OutputConnector outputConnector;

    protected SubstituterNode(Graph graph, String providerId, String name) {
        super(graph, providerId, name,
                ImmutableList.of(new InputConnector("analyzedText", "Analyzed Text", ConnectorType.TEXT)),
                ImmutableList.of(new OutputConnector("analyzedText", "Analyzed Text", ConnectorType.TEXT)));

        outputConnector = getOutputConnectors().get(0);
    }

    @Override
    public final void push(InputConnector connector, Object data) {
        Text newData = substitute((Text) data);
        sendOutput(outputConnector, newData);
    }
}
