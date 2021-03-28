package org.trypticon.talker.text;

import java.net.URL;

public class Emoticon extends Token {
    private final URL resource;

    public Emoticon(URL resource) {
        super("\uFFFC", TokenType.EMOTICON);
        this.resource = resource;
    }

    public URL getResource() {
        return resource;
    }
}
