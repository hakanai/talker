package org.trypticon.talker.speech.sapi;

import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.model.GraphLocation;
import org.trypticon.talker.speech.SpeakerNode;
import org.trypticon.talker.text.Text;

/**
 * Speaker using Microsoft's Speech API (SAPI).
 */
public class SapiSpeakerNode extends SpeakerNode {
    private final String voice;
    private final int rate;
    private final boolean force32Bit;

    public SapiSpeakerNode(GraphLocation graphLocation, String providerId, Configuration configuration) {
        super(graphLocation, providerId, "Speaker: SAPI");

        voice = configuration.getOptionalString("voice").orElse(null);
        rate = configuration.getOptionalInt("rate").orElse(0);
        force32Bit = configuration.getOptionalBoolean("force32Bit").orElse(false);
    }

    @Override
    public void populateConfiguration(Configuration.Builder builder) {
        builder.putIfNotNull("voice", voice)
                .put("rate", rate)
                .put("force32Bit", force32Bit);
    }

    @Override
    public void speak(Text text) {
        SapiUtils.speak(text.getPlainTextContent(), rate);
    }
}
