package talker.speech.mac;

import talker.speech.Speaker;
import talker.speech.util.ProcessUtils;
import talker.text.Text;

import java.io.IOException;

/**
 * Speaker using the 'say' command on OSX to do the speech.
 */
public class MacSpeaker implements Speaker {
    private final String voice;

    public MacSpeaker(String voice) {
        this.voice = voice;
    }

    @Override
    public void speak(Text text) {
        ProcessUtils.execAndWait("say", "-v", voice, text.getContent());
    }
}
