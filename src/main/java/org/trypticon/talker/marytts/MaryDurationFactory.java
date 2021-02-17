package org.trypticon.talker.marytts;

import java.util.List;

import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;

/**
 * Convenience wrapper around MaryTTS to produce sequences of phoneme durations.
 */
public class MaryDurationFactory {
    private final MaryInterface mary;

    public MaryDurationFactory() {
        try {
            mary = MaryUtils.createMary();
            mary.setOutputType("REALISED_DURATIONS");
        } catch (MaryConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public List<MaryDuration> generateReading(String text) {
        try {
            return MaryDuration.fromLines(mary.generateText(text));
        } catch (SynthesisException e) {
            throw new RuntimeException("Error synthesising reading for text: " + text, e);
        }
    }
}
