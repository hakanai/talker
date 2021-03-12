package org.trypticon.talker.speech;

import org.jetbrains.annotations.Nullable;
import org.trypticon.talker.TalkerContext;
import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.model.Graph;
import org.trypticon.talker.model.Node;
import org.trypticon.talker.model.NodeFactory;
import org.trypticon.talker.speech.mac.MacSpeakerNode;
import org.trypticon.talker.speech.marytts.MaryTTSSpeakerNode;
import org.trypticon.talker.speech.robokoe.RoboKoeSpeakerNode;
import org.trypticon.talker.speech.sapi.SapiSpeakerNode;

public class SpeakerNodeFactory implements NodeFactory {
    @Nullable
    @Override
    public Node create(Graph graph, TalkerContext context, String providerId, Configuration configuration) {
        switch (providerId) {
            case "speaker_macos":
                return new MacSpeakerNode(graph, providerId, configuration);
            case "speaker_miku_speak":
                return new RoboKoeSpeakerNode(graph, providerId, configuration);
            case "speaker_sapi":
                return new SapiSpeakerNode(graph, providerId, configuration);
            case "speaker_marytts":
                return new MaryTTSSpeakerNode(graph, providerId, configuration);
            default:
                return null;
        }
    }
}
