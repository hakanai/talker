package org.trypticon.talker.messages;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Base class for message streams.
 */
public abstract class AbstractMessageStream implements MessageStream {
    private final List<MessageStreamListener> listeners = new CopyOnWriteArrayList<>();

    @Override
    public final void addMessageStreamListener(MessageStreamListener listener) {
        listeners.add(listener);
    }

    @Override
    public final void removeMessageStreamListener(MessageStreamListener listener) {
        listeners.remove(listener);
    }

    protected final void fireMessageReceived(Message message) {
        MessageStreamEvent event = new MessageStreamEvent(this, message);
        for (MessageStreamListener listener : listeners) {
            listener.messageReceived(event);
        }
    }
}
