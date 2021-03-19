package org.trypticon.talker.model;

import javax.annotation.Nullable;

import org.trypticon.talker.TalkerContext;
import org.trypticon.talker.config.Configuration;

/**
 * Interface to create nodes.
 */
@FunctionalInterface
public interface NodeFactory {

    /**
     * Called to create the node.
     *
     * @param graphLocation the containing graph.
     * @param context the application context.
     * @param providerId a string ID identifying which provider to use.
     * @param configuration the configuration for the node.
     * @return the node. Returns {@code null} if this factory can't handle the provider ID.
     */
    @Nullable
    Node create(GraphLocation graphLocation, TalkerContext context, String providerId, Configuration configuration);
}
