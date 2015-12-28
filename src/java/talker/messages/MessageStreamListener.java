package talker.messages;

import java.util.EventListener;

/**
 * Event listener for UStream social stream.
 */
public interface MessageStreamListener extends EventListener {
    void refreshStarted(MessageStreamEvent event);
    void refreshFinished(MessageStreamEvent event);
    void refreshFailed(MessageStreamEvent event);
    void messageReceived(MessageStreamEvent event);
}
