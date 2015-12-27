package org.trypticon.talker.messages;

import java.time.Instant;

/**
 * Message container.
 */
public class Message {
    private final Instant timestamp;
    private final String speaker;
    private final String text;

    public Message(Instant timestamp, String speaker, String text) {
        this.timestamp = timestamp;
        this.speaker = speaker;
        this.text = text;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getSpeaker() {
        return speaker;
    }

    public String getText() {
        return text;
    }
}
