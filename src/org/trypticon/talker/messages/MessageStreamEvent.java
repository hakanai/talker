package org.trypticon.talker.messages;

import java.util.EventObject;

/**
 * Event for a message.
 */
public class MessageStreamEvent extends EventObject {
    private final Message message;

    public MessageStreamEvent(MessageStream stream, Message message) {
        super(stream);
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
