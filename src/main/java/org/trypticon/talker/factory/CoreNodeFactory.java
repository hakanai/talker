package org.trypticon.talker.factory;

import org.jetbrains.annotations.Nullable;
import org.trypticon.talker.TalkerContext;
import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.messages.twitch.TwitchMessagesNode;
import org.trypticon.talker.model.GraphLocation;
import org.trypticon.talker.model.Node;
import org.trypticon.talker.model.NodeFactory;
import org.trypticon.talker.rendering.RenderMessagesNode;
import org.trypticon.talker.speech.mac.MacSpeakerNode;
import org.trypticon.talker.speech.marytts.MaryTTSSpeakerNode;
import org.trypticon.talker.speech.robokoe.RoboKoeSpeakerNode;
import org.trypticon.talker.speech.sapi.SapiSpeakerNode;
import org.trypticon.talker.text.substitution.KatakanaReadingSubstituterNode;
import org.trypticon.talker.text.substitution.RegexSubstituterNode;
import org.trypticon.talker.text.tokenisation.KuromojiTokenizerNode;

/**
 * Factory for creating all the nodes the core app supports.
 */
public class CoreNodeFactory implements NodeFactory {
    @Nullable
    @Override
    public Node create(GraphLocation graphLocation, TalkerContext context, String providerId, Configuration configuration) {
        switch (providerId) {

            case "message_stream_twitch":
                return new TwitchMessagesNode(graphLocation, providerId, configuration);

            case "tokenizer_kuromoji":
                return new KuromojiTokenizerNode(graphLocation, providerId);

            case "render_messages":
                return new RenderMessagesNode(graphLocation, providerId, context.getView());

            case "substituter_katakana_reading":
                return new KatakanaReadingSubstituterNode(graphLocation, providerId);
            case "substituter_regex":
                return new RegexSubstituterNode(graphLocation, providerId, configuration);

            case "speaker_macos":
                return new MacSpeakerNode(graphLocation, providerId, configuration);
            case "speaker_miku_speak":
                return new RoboKoeSpeakerNode(graphLocation, providerId, configuration);
            case "speaker_sapi":
                return new SapiSpeakerNode(graphLocation, providerId, configuration);
            case "speaker_marytts":
                return new MaryTTSSpeakerNode(graphLocation, providerId, configuration);

            default:
                return null;
        }
    }
}
