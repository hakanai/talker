package org.trypticon.talker.speech.robokoe.text;

import java.util.Locale;

/**
 * A slice of text output by Kuromoji.
 */
class KuromojiSlice {
    private final String text;
    private final Type type;

    KuromojiSlice(String text, Type type) {
        this.text = text;
        this.type = type;
    }

    String getText() {
        return text;
    }

    Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "%s[%s, %s]", getClass().getSimpleName(), text, type);
    }

    enum Type {
        JAPANESE,
        PUNCTUATION,
        OTHER
    }
}
