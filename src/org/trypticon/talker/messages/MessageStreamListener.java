package org.trypticon.talker.messages;

import java.util.EventListener;

/**
 * Event listener for UStream social stream.
 */
public interface MessageStreamListener extends EventListener {
    void messageReceived(MessageStreamEvent event);
}
