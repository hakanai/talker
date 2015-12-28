package talker.speech.robokoe;

import talker.speech.Speaker;
import talker.speech.util.ProcessUtils;
import talker.text.Text;
import talker.text.substitution.KatakanaReadingSubstituter;

import java.io.IOException;

/**
 * Speaker using {@code miku_speak.exe} AKA "èââπÉ~ÉNÇÃÉçÉ{ê∫" to do the speech.
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
