package talker.speech;

import talker.config.Configuration;
import talker.speech.mac.MacSpeaker;

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
            default:
                throw new IllegalArgumentException("Unknown voice provider: " + providerName);
        }
    }
}
