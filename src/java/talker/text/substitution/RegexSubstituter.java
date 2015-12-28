package talker.text.substitution;

import talker.text.Token;
import talker.text.TokenType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Substitutes text based on a regex.
 */
public class RegexSubstituter extends TokenByTokenSubstituter {
    private final TokenType inputType;
    private final TokenType outputType;
    private final Pattern regex;
    private final String replacement;

    public RegexSubstituter(TokenType inputType, TokenType outputType, String regex, String replacement) {
        this.inputType = inputType;
        this.outputType = outputType;
        this.regex = Pattern.compile(regex);
        this.replacement = replacement;
    }

    @Override
    protected Token substitute(Token token) {
        if (token.getType() == inputType) {
            Matcher matcher = regex.matcher(token.getContent());
            if (matcher.matches()) {
                return new Token(matcher.replaceFirst(replacement), outputType);
            }
        }
        return token;
    }
}
