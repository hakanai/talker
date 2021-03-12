package org.trypticon.talker.speech.mac;

import javax.annotation.Nullable;

import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.model.Graph;
import org.trypticon.talker.speech.SpeakerNode;
import org.trypticon.talker.speech.util.ProcessUtils;
import org.trypticon.talker.text.Text;

/**
 * Speaker using the 'say' command on OSX to do the speech.
 */
public class MacSpeakerNode extends SpeakerNode {
    @Nullable
    private final String voice;

    public MacSpeakerNode(Graph graph, String providerId, Configuration configuration) {
        super(graph, providerId, "Speaker: macOS");

        voice = configuration.getOptionalString("voice").orElse(null);
    }

    @Override
    public void populateConfiguration(Configuration.Builder builder) {
        builder.putIfNotNull("voice", voice);
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
