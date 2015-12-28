package talker.messages;

import talker.config.Configuration;
import talker.messages.ustream.UStreamMessageStream;

/**
 * Responsible for creating a message stream.
 */
public class MessageStreamFactory {
    public MessageStream create(Configuration configuration) {
        Configuration messagesConfiguration = configuration.getSubSection("messages");
        String providerName = messagesConfiguration.getString("provider");
        switch (providerName) {
            case "ustream": {
                int channelId = messagesConfiguration.getInt("channelId");
                return new UStreamMessageStream(channelId);
            }
            default:
                throw new IllegalArgumentException("Unknown voice provider: " + providerName);
        }
    }
}
