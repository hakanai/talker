package talker.text.substitution;

import talker.config.Configuration;
import talker.text.TokenType;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for creating a substituter.
 */
public class SubstituterFactory {
    public Substituter create(Configuration configuration) {
        List<Configuration> substitutionConfigurations = configuration.getSubSectionList("substitutions");
        List<Substituter> constituents = new ArrayList<>(substitutionConfigurations.size());
        for (Configuration substitutionConfiguration : substitutionConfigurations) {
            String providerName = substitutionConfiguration.getString("provider");
            switch (providerName) {
                case "regex":
                    constituents.add(new RegexSubstituter(
                            TokenType.valueOf(substitutionConfiguration.getString("inputType")),
                            TokenType.valueOf(substitutionConfiguration.getString("outputType")),
                            substitutionConfiguration.getString("regex"),
                            substitutionConfiguration.getString("replacement")));
            }
        }
        return new CompositeSubstituter(constituents);
    }
}
