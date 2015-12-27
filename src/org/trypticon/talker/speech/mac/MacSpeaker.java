package org.trypticon.talker.speech.mac;

import org.trypticon.talker.speech.Speaker;

import java.io.IOException;

/**
 * Speaker using the 'say' command on OSX to do the speech.
 */
public class MacSpeaker implements Speaker {
    @Override
    public void speak(String text) {
        try {
            Process process = new ProcessBuilder("say", "-v", "Kyoko", text)
                    .start();
            process.waitFor();
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't launch speech program, perhaps you're not on OSX somehow?", e);
        } catch (InterruptedException e) {
            // No problem.
            Thread.currentThread().interrupt();
        }
    }
}
