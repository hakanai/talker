package org.trypticon.talker.text.substituter;

import org.trypticon.talker.text.Text;
import org.trypticon.talker.text.Token;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A substituter which works by substituting each token in turn.
 */
public abstract class TokenByTokenSubstituter implements Substituter {
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
