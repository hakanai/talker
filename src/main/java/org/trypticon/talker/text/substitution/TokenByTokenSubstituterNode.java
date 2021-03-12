package org.trypticon.talker.text.substitution;

import java.util.List;
import java.util.stream.Collectors;

import org.trypticon.talker.model.Graph;
import org.trypticon.talker.text.Text;
import org.trypticon.talker.text.Token;

/**
 * A substituter which works by substituting each token in turn.
 */
public abstract class TokenByTokenSubstituterNode extends SubstituterNode {
    protected TokenByTokenSubstituterNode(Graph graph, String name) {
        super(graph, name);
    }

    @Override
    public final Text substitute(Text text) {
        List<Token> newTokens = text.stream()
                .map(this::substitute)
                .collect(Collectors.toList());
        return new Text(newTokens);
    }

    /**
     * Implemented by subclasses to substitute an individual token.
     *
     * @param token the token.
     * @return the new token. Could be the same token passed in.
     */
    protected abstract Token substitute(Token token);
}
