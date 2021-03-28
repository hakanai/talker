package org.trypticon.talker.text;

import java.net.URL;

import com.google.common.html.HtmlEscapers;

public class Emoticon extends Token {
    private final URL resource;

    public Emoticon(URL resource) {
        super("\uFFFC", TokenType.EMOTICON);
        this.resource = resource;
    }

    public URL getResource() {
        return resource;
    }

    @Override
    public String getHyperTextContent() {
        return "<img src=\"" + HtmlEscapers.htmlEscaper().escape(resource.toExternalForm()) + "\">";
    }
}
