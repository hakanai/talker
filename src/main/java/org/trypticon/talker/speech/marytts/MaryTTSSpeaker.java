package org.trypticon.talker.speech.marytts;

import java.io.IOException;
import java.io.UncheckedIOException;
import javax.sound.sampled.AudioInputStream;

import marytts.util.data.audio.AudioPlayer;
import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.marytts.MaryAudioFactory;
import org.trypticon.talker.speech.Speaker;
import org.trypticon.talker.text.Text;

/**
 * Speaker using the MaryTTS library to do the speech.
 */
public class MaryTTSSpeaker implements Speaker {
    private final MaryAudioFactory wrapper;

    public MaryTTSSpeaker(Configuration speakerConfiguration) {
        wrapper = new MaryAudioFactory(speakerConfiguration.getString("voice"));
    }

    @Override
    public void speak(Text text) {
        try (AudioInputStream inputStream = wrapper.generateAudio(text.getContent())) {
            new AudioPlayer(inputStream).start();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
