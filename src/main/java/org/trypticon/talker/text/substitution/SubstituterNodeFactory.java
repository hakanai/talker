package org.trypticon.talker.text.substitution;

import java.awt.*;
import javax.annotation.Nullable;

import org.trypticon.talker.TalkerContext;
import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.model.GraphLocation;
import org.trypticon.talker.model.Node;
import org.trypticon.talker.model.NodeFactory;

/**
 * Responsible for creating a substituter.
 */
public class SubstituterNodeFactory implements NodeFactory {
    @Override
    @Nullable
    public Node create(GraphLocation graphLocation, TalkerContext context, String providerId, Configuration configuration) {
        switch (providerId) {
            case "substituter_katakana_reading":
                return new KatakanaReadingSubstituterNode(graphLocation, providerId);
            case "substituter_regex":
                return new RegexSubstituterNode(graphLocation, providerId, configuration);
            default:
                return null;
        }
    }
}
