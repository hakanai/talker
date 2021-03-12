package org.trypticon.talker.speech.robokoe;

import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.speech.Speaker;
import org.trypticon.talker.speech.util.ProcessUtils;
import org.trypticon.talker.text.Text;

/**
 * Speaker using {@code miku_speak.exe} AKA "�����~�N�̃��{��" to do the speech.
 */
public class RoboKoeSpeaker implements Speaker {
    private final String executable;

    public RoboKoeSpeaker(String executable) {
        this.executable = executable;
    }

    public RoboKoeSpeaker(Configuration configuration) {
        this(configuration.getRequiredString("executable"));
   }

    @Override
    public String getId() {
        return "speaker_miku_speak";
    }

    @Override
    public String getName() {
        return "miku_speak";
    }

    @Override
    public void speak(Text text) {

        //TODO: This process exits before it has finished. :(
        ProcessUtils.execAndWait(executable, text.getContent());

    }
}
