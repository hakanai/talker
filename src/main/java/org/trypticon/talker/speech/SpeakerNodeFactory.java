package org.trypticon.talker.speech;

import org.jetbrains.annotations.Nullable;
import org.trypticon.talker.TalkerContext;
import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.model.Graph;
import org.trypticon.talker.model.Node;
import org.trypticon.talker.model.NodeFactory;
import org.trypticon.talker.speech.mac.MacSpeaker;
import org.trypticon.talker.speech.marytts.MaryTTSSpeaker;
import org.trypticon.talker.speech.robokoe.RoboKoeSpeaker;
import org.trypticon.talker.speech.sapi.SapiSpeaker;

public class SpeakerNodeFactory implements NodeFactory {
    @Nullable
    @Override
    public Node create(Graph graph, TalkerContext context, String providerId, Configuration configuration) {
        Speaker speaker = createSpeaker(providerId, configuration);
        return new SpeakerNode("Speaker: " + speaker.getName(), speaker);
    }

    private Speaker createSpeaker(String providerId, Configuration configuration) {
        switch (providerId) {
            case "speaker_macos":
                return new MacSpeaker(configuration);
            case "speaker_miku_speak":
                return new RoboKoeSpeaker(configuration);
            case "speaker_sapi":
                return new SapiSpeaker(configuration);
            case "speaker_marytts":
                return new MaryTTSSpeaker(configuration);
            default:
                return null;
        }
    }
}
