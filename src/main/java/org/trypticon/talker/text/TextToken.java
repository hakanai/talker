package org.trypticon.talker.text;

import com.google.common.html.HtmlEscapers;

/**
 * Representation of a single token of text.
 */
public final class TextToken extends Token {

    public TextToken(String content, TokenType type) {
        super(content, type);
    }

    @Override
    public String getHyperTextContent() {
        return HtmlEscapers.htmlEscaper().escape(getPlainTextContent());
    }
}
