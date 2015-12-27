package org.trypticon.talker.speech.robokoe.text;

import java.util.List;

/**
 * Fills in Katakana for words which Kuromoji can't read.
 */
public class KatakanaFiller {
    private final KuromojiWrapper kuromoji = new KuromojiWrapper();
    private final KatakanaGenerator katakana = new KatakanaGenerator();

    public String fill(String text) {
        StringBuilder result = new StringBuilder();
        List<KuromojiSlice> slices = kuromoji.slice(text);
        for (KuromojiSlice slice : slices) {
            switch (slice.getType()) {
                case JAPANESE:
                    result.append(slice.getText());
                    break;

                case PUNCTUATION:
                    result.append(katakana.punctuationToJapanese(slice.getText()));
                    break;

                case OTHER:
                    result.append(katakana.englishToKatakana(slice.getText()));
                    break;
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(new KatakanaFiller().fill("Hello. My name is 初音ミク. My lucky number is 39."));
    }
}
