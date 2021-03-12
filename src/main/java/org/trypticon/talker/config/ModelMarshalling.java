package org.trypticon.talker.config;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import org.trypticon.talker.TalkerContext;
import org.trypticon.talker.messages.MessageStreamNodeFactory;
import org.trypticon.talker.model.*;
import org.trypticon.talker.rendering.RenderingNodeFactory;
import org.trypticon.talker.speech.SpeakerNodeFactory;
import org.trypticon.talker.text.substitution.SubstituterNodeFactory;
import org.trypticon.talker.text.tokenisation.TokenizerNodeFactory;

/**
 * Utility to load a model from the top-level configuration file.
 */
public class ModelMarshalling {
    private ModelMarshalling() {}

    public static Configuration storeGraph(Graph graph) {
        // TODO: Saving to disk. Requires Configuration to be mutable, or possibly use builders?
        throw new UnsupportedOperationException("TODO");
    }

    public static Graph loadGraph(TalkerContext context, Configuration topConfiguration) {
        Graph graph = new Graph();

        Map<Integer, Node> nodesById = new LinkedHashMap<>();

        List<Configuration> nodeConfigurations = topConfiguration.getSubSectionList("nodes");
        for (Configuration nodeConfiguration : nodeConfigurations) {
            Node node = loadNode(graph, context, nodeConfiguration);
            graph.add(node);
            nodesById.put(nodeConfiguration.getRequiredInt("id"), node);
        }

        List<Configuration> connectionConfigurations = topConfiguration.getSubSectionList("connections");
        for (Configuration connectionConfiguration : connectionConfigurations) {
            Connection connection = loadConnection(nodesById, connectionConfiguration);
            graph.add(connection);
        }

        return graph;
    }

    private static Node loadNode(Graph graph, TalkerContext context, Configuration configuration) {
        ImmutableList<NodeFactory> nodeFactories = ImmutableList.of(
                new MessageStreamNodeFactory(),
                new RenderingNodeFactory(),
                new TokenizerNodeFactory(),
                new SubstituterNodeFactory(),
                new SpeakerNodeFactory());
        String providerId = configuration.getRequiredString("providerId");
        Configuration innerConfiguration = configuration.getSubSection("config");
        for (NodeFactory factory : nodeFactories) {
            Node node = factory.create(graph, context, providerId, innerConfiguration);
            if (node != null) {
                return node;
            }
        }
        throw new IllegalStateException("Unknown node provider: " + providerId);
    }

    private static Connection loadConnection(Map<Integer, Node> nodesById, Configuration configuration) {
        Node sourceNode = nodesById.get(configuration.getRequiredInt("fromNode"));
        OutputConnector sourceConnector = sourceNode.getOutputConnectorById(configuration.getRequiredString("fromConnector"));
        Node targetNode = nodesById.get(configuration.getRequiredInt("toNode"));
        InputConnector targetConnector = targetNode.getInputConnectorById(configuration.getRequiredString("toConnector"));
        return new Connection(sourceConnector, targetConnector);
    }
}
