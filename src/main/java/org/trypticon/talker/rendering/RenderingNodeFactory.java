package org.trypticon.talker.rendering;

import javax.annotation.Nullable;

import org.trypticon.talker.TalkerContext;
import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.model.Graph;
import org.trypticon.talker.model.Node;
import org.trypticon.talker.model.NodeFactory;


/**
 * Factory for rendering nodes.
 */
public class RenderingNodeFactory implements NodeFactory {
    @Nullable
    @Override
    public Node create(Graph graph, TalkerContext context, String providerId, Configuration configuration) {
        switch (providerId) {
            case "render_message":
                return new RenderMessageNode(context.getView());
            default:
                return null;
        }
    }
}
