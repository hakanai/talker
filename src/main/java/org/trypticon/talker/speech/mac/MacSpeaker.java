package org.trypticon.talker.speech.mac;

import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.speech.Speaker;
import org.trypticon.talker.speech.util.ProcessUtils;
import org.trypticon.talker.text.Text;

/**
 * Speaker using the 'say' command on OSX to do the speech.
 */
public class MacSpeaker implements Speaker {
    private final String voice;

    public MacSpeaker(String voice) {
        this.voice = voice;
    }

    public MacSpeaker(Configuration configuration) {
        this(configuration.getOptionalString("voice", null));
    }

    @Override
    public String getId() {
        return "speaker_macos";
    }

    @Override
    public String getName() {
        return "macOS";
    }

    @Override
    public void speak(Text text) {
        if (voice == null) {
            ProcessUtils.execAndWait("say", text.getContent());
        } else {
            ProcessUtils.execAndWait("say", "-v", voice, text.getContent());
        }
    }
}
