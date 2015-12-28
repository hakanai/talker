package talker.speech;

import talker.config.Configuration;
import talker.speech.mac.MacSpeaker;
import talker.speech.robokoe.RoboKoeSpeaker;

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
