package org.trypticon.talker.speech;

import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.speech.mac.MacSpeaker;
import org.trypticon.talker.speech.marytts.MaryTTSSpeaker;
import org.trypticon.talker.speech.robokoe.RoboKoeSpeaker;
import org.trypticon.talker.speech.sapi.SapiSpeaker;

/**
 * Responsible for creating a speaker.
 */
public class SpeakerFactory {
    public Speaker create(Configuration configuration) {
        Configuration speakerConfiguration = configuration.getSubSection("speaker");
        String providerName = speakerConfiguration.getString("provider");
        switch (providerName) {
            case "mac":
                return new MacSpeaker(speakerConfiguration);
            case "robokoe":
                return new RoboKoeSpeaker(speakerConfiguration);
            case "sapi":
                return new SapiSpeaker(speakerConfiguration);
            case "marytts":
                return new MaryTTSSpeaker(speakerConfiguration);
            default:
                throw new IllegalArgumentException("Unknown voice provider: " + providerName);
        }
    }
}
