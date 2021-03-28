package org.trypticon.talker.text;

/**
 * Representation of a single token of text.
 */
public abstract class Token {
    private final String content;
    private final TokenType type;

    /**
     * Package local constructor means that we know about all subclasses.
     *
     * @param content
     * @param type
     */
    Token(String content, TokenType type) {
        this.content = content;
        this.type = type;
    }

    public String getPlainTextContent() {
        return content;
    }

    public abstract String getHyperTextContent();

    public TokenType getType() {
        return type;
    }
}
