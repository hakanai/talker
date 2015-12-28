package org.trypticon.talker.speech;

import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.speech.mac.MacSpeaker;
import org.trypticon.talker.speech.robokoe.RoboKoeSpeaker;

/**
 * Responsible for creating a speaker.
 */
public class SpeakerFactory {
    public Speaker create(Configuration configuration) {
        Configuration speakerConfiguration = configuration.getSubSection("speaker");
        String providerName = speakerConfiguration.getString("provider");
        switch (providerName) {
            case "mac": {
                String voice = speakerConfiguration.getString("voice");
                return new MacSpeaker(voice);
            }
            case "robokoe": {
                String executable = speakerConfiguration.getString("executable");
                return new RoboKoeSpeaker(executable);
            }
            default:
                throw new IllegalArgumentException("Unknown voice provider: " + providerName);
        }
    }
}
