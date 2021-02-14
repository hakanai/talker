package org.trypticon.talker.text.substitution;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;

import java.util.List;
import java.util.Locale;

/**
 * Convenience wrapper around MaryTTS.
 */
class MaryWrapper {
    private final MaryInterface mary;

    MaryWrapper() {
        try {
            mary = new LocalMaryInterface();

            // You get an NPE if you don't set a voice when setting a locale.
            mary.setLocale(new Locale("en", "GB"));
            mary.setVoice(mary.getAvailableVoices().iterator().next());

            mary.setOutputType("REALISED_DURATIONS");

        } catch (MaryConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    List<MaryDuration> generate(String text) {
        try {
            return MaryDuration.fromLines(mary.generateText(text));
        } catch (SynthesisException e) {
            throw new RuntimeException("Error synthesising reading for text: " + text, e);
        }
    }
}
