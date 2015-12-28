package talker.text.tokenisation;

import org.atilika.kuromoji.Tokenizer;
import talker.text.Text;
import talker.text.Token;
import talker.text.TokenType;

import java.util.LinkedList;
import java.util.List;

/**
 * Tokeniser backed by Kuromoji.
 */
public class KuromojiTokeniser {
    private final Tokenizer tokeniser;

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
        String pos = token.getAllFeaturesArray()[1];
        TokenType tokenType;
        switch (pos) {
            case "アルファベット":
                tokenType = TokenType.OTHER;
                break;
            case "サ変接続":
                tokenType = TokenType.PUNCTUATION;
                break;
            case "空白":
                tokenType = TokenType.WHITESPACE;
                break;
            default:
                if (token.getAllFeaturesArray().length > 7) {
                    tokenType = TokenType.JAPANESE;
                } else {
                    tokenType = TokenType.OTHER;
                }
        }

        return new Token(token.getSurfaceForm(), tokenType);
    }
}
