package org.trypticon.talker.messages;

import java.net.URL;
import java.time.Instant;

/**
 * Message container.
 */
public class Message {
    private final Instant timestamp;
    private final String speaker;
    private final URL speakerIcon;
    private final String text;

    public Message(Instant timestamp, String speaker, URL speakerIcon, String text) {
        this.timestamp = timestamp;
        this.speaker = speaker;
        this.speakerIcon = speakerIcon;
        this.text = text;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getSpeaker() {
        return speaker;
    }

    public URL getSpeakerIcon() {
        return speakerIcon;
    }

    public String getText() {
        return text;
    }
}
