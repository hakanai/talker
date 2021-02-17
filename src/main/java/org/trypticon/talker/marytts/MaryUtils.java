package org.trypticon.talker.marytts;

import java.util.Locale;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import org.jetbrains.annotations.Nullable;

public class MaryUtils {
    public static MaryInterface createMary() throws MaryConfigurationException {
        return createMary(null);
    }

    public static MaryInterface createMary(@Nullable String voice) throws MaryConfigurationException {
        MaryInterface mary = new LocalMaryInterface();

        // You get an NPE if you don't set a voice when setting a locale.
        mary.setLocale(new Locale("en", "GB"));

        if (voice == null) {
            voice = mary.getAvailableVoices().iterator().next();
        }

        mary.setVoice(voice);

        return mary;
    }
}
