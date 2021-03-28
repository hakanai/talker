package org.trypticon.talker.messages;

import java.net.URL;
import java.time.Instant;

import org.trypticon.talker.text.Text;
import org.trypticon.talker.text.TextToken;
import org.trypticon.talker.text.TokenType;

/**
 * Message container.
 */
public class Message {
    private final Instant timestamp;
    private final String speaker;
    private final URL speakerIcon;
    private final Text text;

    public Message(Instant timestamp, String speaker, URL speakerIcon, String text) {
        this(timestamp, speaker, speakerIcon,
                new Text(new TextToken(text, TokenType.UNRECOGNISED)));
    }

    public Message(Instant timestamp, String speaker, URL speakerIcon, Text text) {
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

    public Text getText() {
        return text;
    }
}
