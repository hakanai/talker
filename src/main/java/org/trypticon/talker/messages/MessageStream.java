package org.trypticon.talker.messages;

/**
 * Where messages come from.
 */
public interface MessageStream {
    void start();
    void stop();

    String getPreferenceSubKey();

    void addMessageStreamListener(MessageStreamListener listener);
    void removeMessageStreamListener(MessageStreamListener listener);

}
