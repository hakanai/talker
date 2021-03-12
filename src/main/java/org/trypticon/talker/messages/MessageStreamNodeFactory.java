package org.trypticon.talker.messages;

import javax.annotation.Nullable;

import org.trypticon.talker.TalkerContext;
import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.messages.ustream.UStreamMessagesNode;
import org.trypticon.talker.model.Graph;
import org.trypticon.talker.model.Node;
import org.trypticon.talker.model.NodeFactory;

/**
 * Responsible for creating a message stream.
 */
public class MessageStreamNodeFactory implements NodeFactory {
    @Nullable
    @Override
    public Node create(Graph graph, TalkerContext context, String providerId, Configuration configuration) {
        switch (providerId) {
            case "message_stream_ustream":
                return new UStreamMessagesNode(graph, providerId, configuration);
            default:
                return null;
        }
    }
}
