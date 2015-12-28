package talker.text;

/**
 * Representation of a single token of text.
 */
public final class Token {
    private final String content;
    private final TokenType type;

    public Token(String content, TokenType type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public TokenType getType() {
        return type;
    }
}
