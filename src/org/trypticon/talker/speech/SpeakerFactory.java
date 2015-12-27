package org.trypticon.talker.speech;

import org.trypticon.talker.speech.mac.MacSpeaker;

import java.util.Properties;

/**
 * Responsible for creating a speaker.
 */
public class SpeakerFactory {
    public Speaker create(Properties config) {
        String providerName = config.getProperty("messages");
        switch (providerName) {
            case "mac": {
                String voice = config.getProperty("speaker.voice");
                return new MacSpeaker(voice);
            }
            default:
                throw new IllegalArgumentException("Unknown voice provider: " + providerName);
        }
    }
}
