package org.trypticon.talker.speech.robokoe.text;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

import java.util.LinkedList;
import java.util.List;

/**
 * Convenience wrapper for Kuromoji.
 */
class KuromojiWrapper {
    private final Tokenizer tokeniser;
    
    KuromojiWrapper() {
        Tokenizer.Builder tokeniserBuilder = Tokenizer.builder();
        tokeniser = tokeniserBuilder.build();
    }
    
    List<KuromojiSlice> slice(String text) {
        List<Token> tokens = tokeniser.tokenize(text);

        StringBuilder builder = new StringBuilder();
        State state = State.START;
        List<KuromojiSlice> results = new LinkedList<>();

        for (Token token : tokens) {
            String pos = token.getAllFeaturesArray()[1];
            State newState;
            if ("サ変接続".equals(pos)) {
                newState = State.IN_PUNCTUATION;
            } else if ("空白".equals(pos)) {
                newState = state; // glue whitespace to the previous token
            } else if (token.getAllFeaturesArray().length > 7) {
                newState = State.IN_JAPANESE;
            } else {
                newState = State.IN_OTHER;
            }

            if (newState != state) {
                flush(builder, state, results);
            }
            state = newState;

            builder.append(token.getSurfaceForm());
        }

        flush(builder, state, results);

        return results;
    }
    
    private void flush(StringBuilder builder, State state, List<KuromojiSlice> results) {
        if (builder.length() > 0) {

            KuromojiSlice.Type type;
            switch (state) {
                case IN_JAPANESE:
                    type = KuromojiSlice.Type.JAPANESE;
                    break;
                case IN_PUNCTUATION:
                    type = KuromojiSlice.Type.PUNCTUATION;
                    break;
                case IN_OTHER:
                    type = KuromojiSlice.Type.OTHER;
                    break;
                case START:
                default:
                    throw new IllegalStateException("In STATE, should be impossible");
            }

            results.add(new KuromojiSlice(builder.toString(), type));
            builder.setLength(0);
        }
    }

    private enum State {
        START, IN_JAPANESE, IN_PUNCTUATION, IN_OTHER
    }
}
