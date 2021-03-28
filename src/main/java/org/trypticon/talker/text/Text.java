package org.trypticon.talker.text;

import java.util.AbstractList;
import java.util.Locale;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

/**
 * Representation of a piece of text.
 */
public class Text extends AbstractList<Token> {
    private final ImmutableList<Token> tokens;

    public Text(Iterable<Token> tokens) {
        this.tokens = ImmutableList.copyOf(tokens);
    }

    public Text(Token... tokens) {
        this.tokens = ImmutableList.copyOf(tokens);
    }

    public String getPlainTextContent() {
        return tokens.stream()
                .map(Token::getContent)
                .collect(Collectors.joining());
    }

    @Override
    public Token get(int index) {
        return tokens.get(index);
    }

    @Override
    public int size() {
        return tokens.size();
    }

    @Override
    public String toString() {
        String tokensString = tokens.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        return String.format(Locale.ROOT, "%s[%s]", getClass().getSimpleName(), tokensString);
    }
}
