package org.trypticon.talker.text.substitution;

import org.trypticon.talker.text.Text;
import org.trypticon.talker.text.Token;
import org.trypticon.talker.text.TokenType;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>Fills in Katakana for words which Kuromoji can't read.</p>
 *
 * <p>Takes the given text, breaks it up into Japanese and non-Japanese parts and converts
 *    the non-Japanese parts to Japanese by filling in readings for everything else.
 *    Or at least everything else we know how to convert...</p>
 */
public class KatakanaReadingSubstituter implements Substituter {
    private final KatakanaGenerator katakana = new KatakanaGenerator();

    @Override
    public Text substitute(Text text) {
        List<Text> slices = divideByTokenType(text);
        List<Token> results = new LinkedList<>();
        for (Text slice : slices) {
            switch (slice.get(0).getType()) {
                case JAPANESE:
                case WHITESPACE:
                    results.addAll(slice);
                    break;

                case PUNCTUATION:
                    results.addAll(katakana.punctuationToJapanese(slice));
                    break;

                case OTHER:
                    results.addAll(katakana.englishToKatakana(slice));
                    break;
            }
        }
        return new Text(results);
    }

    /**
     * Divides the text into slices which each have the same token type.
     *
     * @param text the input text.
     * @return potentially multiple slices of output text.
     */
    private List<Text> divideByTokenType(Text text) {
        TokenType lastTokenType = null;
        List<Token> currentRun = new LinkedList<>();
        List<Text> results = new LinkedList<>();

        for (Token token : text) {
            TokenType currentTokenType = token.getType();
            if (lastTokenType != null && currentTokenType != lastTokenType) {
                flush(currentRun, results);
            }
            lastTokenType = currentTokenType;
            currentRun.add(token);
        }

        flush(currentRun, results);

        return results;
    }

    /**
     * Flushes current tokens into the results if there are any.
     *
     * @param currentRun the current run of tokens.
     * @param results the results.
     */
    private void flush(List<Token> currentRun, List<Text> results) {
        if (!currentRun.isEmpty()) {
            results.add(new Text(currentRun));
            currentRun.clear();
        }
    }
}
