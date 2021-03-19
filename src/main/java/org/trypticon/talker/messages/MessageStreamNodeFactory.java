package org.trypticon.talker.messages;

import javax.annotation.Nullable;

import org.trypticon.talker.TalkerContext;
import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.messages.twitch.TwitchMessagesNode;
import org.trypticon.talker.model.GraphLocation;
import org.trypticon.talker.model.Node;
import org.trypticon.talker.model.NodeFactory;

/**
 * Responsible for creating a message stream.
 */
public class MessageStreamNodeFactory implements NodeFactory {
    @Nullable
    @Override
    public Node create(GraphLocation graphLocation, TalkerContext context, String providerId, Configuration configuration) {
        switch (providerId) {
            case "message_stream_twitch":
                return new TwitchMessagesNode(graphLocation, providerId, configuration);
            default:
                return null;
        }
    }
}
