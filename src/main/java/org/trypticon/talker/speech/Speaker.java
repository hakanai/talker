package org.trypticon.talker.speech;

import org.trypticon.talker.text.Text;

/**
 * Interface to abstract something which can speak text.
 */
public interface Speaker {

    /**
     * Gets a unique ID for the provider.
     *
     * @return the provider ID.
     */
    String getId();

    /**
     * Gets a readable name for the speaker.
     *
     * @return the name.
     */
    String getName();

    /**
     * Speaks some text. It's supposed to block until done doing so.
     *
     * @param text the text.
     */
    void speak(Text text);
}
