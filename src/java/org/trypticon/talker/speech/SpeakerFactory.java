package org.trypticon.talker.speech;

import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.speech.mac.MacSpeaker;
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
            case "mac": {
                String voice = speakerConfiguration.getString("voice");
                return new MacSpeaker(voice);
            }
            case "robokoe": {
                String executable = speakerConfiguration.getString("executable");
                return new RoboKoeSpeaker(executable);
            }
            case "sapi": {
                String voice = speakerConfiguration.getString("voice");
                int rate = speakerConfiguration.getInt("rate");
                boolean force32Bit = speakerConfiguration.getBoolean("force32Bit");
                return new SapiSpeaker(voice, rate, force32Bit);
            }
            default:
                throw new IllegalArgumentException("Unknown voice provider: " + providerName);
        }
    }
}
