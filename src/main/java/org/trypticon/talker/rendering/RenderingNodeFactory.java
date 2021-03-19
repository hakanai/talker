package org.trypticon.talker.rendering;

import javax.annotation.Nullable;

import org.trypticon.talker.TalkerContext;
import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.model.GraphLocation;
import org.trypticon.talker.model.Node;
import org.trypticon.talker.model.NodeFactory;


/**
 * Factory for rendering nodes.
 */
public class RenderingNodeFactory implements NodeFactory {
    @Nullable
    @Override
    public Node create(GraphLocation graphLocation, TalkerContext context, String providerId, Configuration configuration) {
        switch (providerId) {
            case "render_messages":
                return new RenderMessagesNode(graphLocation, providerId, context.getView());
            default:
                return null;
        }
    }
}
