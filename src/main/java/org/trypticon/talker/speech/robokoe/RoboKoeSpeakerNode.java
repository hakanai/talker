package org.trypticon.talker.speech.robokoe;

import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.model.GraphLocation;
import org.trypticon.talker.speech.SpeakerNode;
import org.trypticon.talker.speech.util.ProcessUtils;
import org.trypticon.talker.text.Text;

/**
 * Speaker using {@code miku_speak.exe} AKA "�����~�N�̃��{��" to do the speech.
 */
public class RoboKoeSpeakerNode extends SpeakerNode {
    private final String executable;

    public RoboKoeSpeakerNode(GraphLocation graphLocation, String providerId, Configuration configuration) {
        super(graphLocation, providerId, "Speaker: RoboKoe");

        this.executable = configuration.getRequiredString("executable");
    }

    @Override
    public void populateConfiguration(Configuration.Builder builder) {
        builder.put("executable", executable);
    }

    @Override
    public void speak(Text text) {
        //TODO: This process exits before it has finished. :(
        ProcessUtils.execAndWait(executable, text.getPlainTextContent());
    }
}
