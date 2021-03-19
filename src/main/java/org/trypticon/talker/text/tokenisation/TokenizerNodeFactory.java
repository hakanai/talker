package org.trypticon.talker.text.tokenisation;

import org.jetbrains.annotations.Nullable;
import org.trypticon.talker.TalkerContext;
import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.model.GraphLocation;
import org.trypticon.talker.model.Node;
import org.trypticon.talker.model.NodeFactory;

public class TokenizerNodeFactory implements NodeFactory {
    @Nullable
    @Override
    public Node create(GraphLocation graphLocation, TalkerContext context, String providerId, Configuration configuration) {
        switch (providerId) {
            case "tokenizer_kuromoji":
                return new KuromojiTokenizerNode(graphLocation, providerId);
            default:
                return null;
        }
    }
}
