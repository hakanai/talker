package talker.text.tokenisation;

import org.atilika.kuromoji.Tokenizer;
import talker.text.Text;
import talker.text.Token;
import talker.text.TokenType;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Tokeniser backed by Kuromoji.
 */
public class KuromojiTokeniser {
    private final Tokenizer tokeniser;
    private final Pattern wordPattern = Pattern.compile("\\w", Pattern.UNICODE_CHARACTER_CLASS);

    public KuromojiTokeniser() {
        Tokenizer.Builder tokeniserBuilder = Tokenizer.builder();
        //TODO: User dictionary would go here.
        tokeniser = tokeniserBuilder.build();
    }

    /**
     * Tokenises text using Kuromoji.
     *
     * @param text the input text.
     * @return the output text.
     */
    public Text tokenise(String text) {
        List<org.atilika.kuromoji.Token> tokens = tokeniser.tokenize(text);
        List<Token> results = new LinkedList<>();

        for (org.atilika.kuromoji.Token token : tokens) {
            results.add(convertToken(token));
        }

        return new Text(results);
    }

    private Token convertToken(org.atilika.kuromoji.Token token) {
        String[] features = token.getAllFeaturesArray();
        String pos = features[0];
        String subPos = features[1];
        String text = token.getSurfaceForm();
        TokenType tokenType = null;

        if ("記号".equals(pos)) {
            switch (subPos) {
                case "アルファベット":
                    // Workaround for these tokens having readings in the dictionary
                    // which we might want to substitute.
                    tokenType = TokenType.OTHER;
                    break;
                case "句点":
                case "読点":
                case "括弧開":
                case "括弧閉":
                case "一般":
                    tokenType = TokenType.PUNCTUATION;
                    break;
                case "空白":
                    tokenType = TokenType.WHITESPACE;
                    break;
                default:
                    // Falls through to null check
            }
        }

        if (tokenType == null) {
            if (!wordPattern.matcher(text).find()) {
                // Workaround for things like "." coming back as "noun".
                tokenType = TokenType.PUNCTUATION;
            } else if (token.getAllFeaturesArray().length > 7) {
                // This basically assumes that if the dictionary knows the reading, it's Japanese.
                // I don't know if they have slipped other non-Japanese stuff into the dictionary...
                tokenType = TokenType.JAPANESE;
            } else {
                tokenType = TokenType.OTHER;
            }
        }

        return new Token(text, tokenType);
    }
}
