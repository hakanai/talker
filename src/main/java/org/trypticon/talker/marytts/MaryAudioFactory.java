package org.trypticon.talker.marytts;

import javax.sound.sampled.AudioInputStream;

import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;

/**
 * Convenience wrapper around MaryTTS to generate audio.
 */
public class MaryAudioFactory {
    private final MaryInterface mary;

    public MaryAudioFactory(String voice) {
        try {
            mary = MaryUtils.createMary(voice);
            mary.setOutputType("AUDIO");
        } catch (MaryConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public AudioInputStream generateAudio(String text) {
        try {
            return mary.generateAudio(text);
        } catch (SynthesisException e) {
            throw new RuntimeException("Error synthesising audio for text: " + text, e);
        }
    }
}
