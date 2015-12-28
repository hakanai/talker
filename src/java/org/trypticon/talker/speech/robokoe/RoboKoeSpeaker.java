package org.trypticon.talker.speech.robokoe;

import org.trypticon.talker.speech.Speaker;
import org.trypticon.talker.speech.util.ProcessUtils;
import org.trypticon.talker.text.Text;
import org.trypticon.talker.text.substitution.KatakanaReadingSubstituter;

/**
 * Speaker using {@code miku_speak.exe} AKA "�����~�N�̃��{��" to do the speech.
 */
public class RoboKoeSpeaker implements Speaker {
    private final String executable;
    private final KatakanaReadingSubstituter readingSubstituter = new KatakanaReadingSubstituter();

    public RoboKoeSpeaker(String executable) {
        this.executable = executable;
    }

    @Override
    public void speak(Text text) {

        // This app can't read non-Japanese characters at all, so we substitute.
        text = readingSubstituter.substitute(text);

        //TODO: This process exits before it has finished. :(
        ProcessUtils.execAndWait(executable, text.getContent());

    }
}
