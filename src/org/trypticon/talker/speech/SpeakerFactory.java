package org.trypticon.talker.speech;

import org.trypticon.talker.speech.mac.MacSpeaker;

/**
 * Responsible for creating a speaker.
 */
public class SpeakerFactory {
    //TODO: Some way to configure it for the future of multiple speakers.
    public Speaker create() {
        String os = System.getProperty("os.name");
        if ("Mac OS X".equals(os)) {
            return new MacSpeaker();
        } else {
            throw new UnsupportedOperationException("I don't know how to speak on this platform");
        }
    }
}
