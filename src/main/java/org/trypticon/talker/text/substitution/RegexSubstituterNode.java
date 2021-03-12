package org.trypticon.talker.text.substitution;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.model.Graph;
import org.trypticon.talker.text.Token;
import org.trypticon.talker.text.TokenType;

/**
 * Substitutes text based on a regex.
 */
public class RegexSubstituterNode extends TokenByTokenSubstituterNode {
    private final TokenType inputType;
    private final TokenType outputType;
    private final Pattern regex;
    private final String replacement;

    public RegexSubstituterNode(Graph graph, TokenType inputType, TokenType outputType, String regex, String replacement) {
        super(graph, "Substitute: Regex");

        this.inputType = inputType;
        this.outputType = outputType;
        this.regex = Pattern.compile(regex);
        this.replacement = replacement;
    }

    public RegexSubstituterNode(Graph graph, Configuration configuration) {
        this(graph,
                TokenType.valueOf(configuration.getRequiredString("inputType")),
                TokenType.valueOf(configuration.getRequiredString("outputType")),
                configuration.getRequiredString("regex"),
                configuration.getRequiredString("replacement"));
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
