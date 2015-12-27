package org.trypticon.talker.messages;

import org.trypticon.talker.messages.ustream.UStreamMessageStream;

import java.util.Properties;

/**
 * Responsible for creating a message stream.
 */
public class MessageStreamFactory {
    public MessageStream create(Properties config) {
        String providerName = config.getProperty("messages");
        switch (providerName) {
            case "ustream": {
                int channelId = Integer.parseInt(config.getProperty("messages.channelId"));
                return new UStreamMessageStream(channelId);
            }
            default:
                throw new IllegalArgumentException("Unknown voice provider: " + providerName);
        }
    }
}
