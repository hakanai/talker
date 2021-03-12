package org.trypticon.talker.speech.marytts;

import java.io.IOException;
import java.io.UncheckedIOException;
import javax.sound.sampled.AudioInputStream;

import marytts.util.data.audio.AudioPlayer;
import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.marytts.MaryAudioFactory;
import org.trypticon.talker.model.Graph;
import org.trypticon.talker.speech.SpeakerNode;
import org.trypticon.talker.text.Text;

/**
 * Speaker using the MaryTTS library to do the speech.
 */
public class MaryTTSSpeakerNode extends SpeakerNode {
    private final String voice;
    private final MaryAudioFactory wrapper;

    public MaryTTSSpeakerNode(Graph graph, String providerId, Configuration speakerConfiguration) {
        super(graph, providerId, "Speaker: MaryTTS");

        voice = speakerConfiguration.getOptionalString("voice").orElse(null);
        wrapper = new MaryAudioFactory(voice);
    }

    @Override
    public void populateConfiguration(Configuration.Builder builder) {
        builder.putIfNotNull("voice", voice);
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
