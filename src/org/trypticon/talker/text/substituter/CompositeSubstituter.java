package org.trypticon.talker.text.substituter;

import org.trypticon.talker.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A substituter which works by running the same tokens through multiple substituters.
 */
public class CompositeSubstituter implements Substituter {
    private final List<Substituter> constituents;

    public CompositeSubstituter(List<Substituter> constituents) {
        this.constituents = new ArrayList<>(constituents);
    }

    @Override
    public Text substitute(Text text) {
        for (Substituter constituent : constituents) {
            text = constituent.substitute(text);
        }
        return text;
    }
}
