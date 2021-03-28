package org.trypticon.talker.text.substitution;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.model.GraphLocation;
import org.trypticon.talker.text.TextToken;
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

    public RegexSubstituterNode(GraphLocation graphLocation, String providerId, Configuration configuration) {
        super(graphLocation, providerId, "Substitute: Regex");

        inputType = TokenType.valueOf(configuration.getRequiredString("inputType"));
        outputType = TokenType.valueOf(configuration.getRequiredString("outputType"));
        regex = Pattern.compile(configuration.getRequiredString("regex"));
        replacement = configuration.getRequiredString("replacement");
    }

    @Override
    public void populateConfiguration(Configuration.Builder builder) {
        builder.put("inputType", inputType.name())
                .put("outputType", outputType.name())
                .put("regex", regex.pattern())
                .put("replacement", replacement);
    }

    @Override
    protected Token substitute(Token token) {
        if (token.getType() == inputType) {
            Matcher matcher = regex.matcher(token.getPlainTextContent());
            if (matcher.matches()) {
                return new TextToken(matcher.replaceFirst(replacement), outputType);
            }
        }
        return token;
    }
}
