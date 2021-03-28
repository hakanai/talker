package org.trypticon.talker.speech.sapi;

import javax.annotation.Nonnull;

import com.sun.jna.platform.win32.COM.COMException;
import com.sun.jna.platform.win32.COM.COMLateBindingObject;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.Variant;

public class SapiUtils {
    private static SpVoice spVoice;

    public static class SpVoice extends COMLateBindingObject {
        private SpVoice() throws COMException {
            super("SAPI.SpVoice", true);
            Runtime.getRuntime().addShutdownHook(new Thread(this::release));
        }

        public void setRate(int rate) {
            setProperty("Rate", rate);
        }

        public void speak(@Nonnull String text) {
            invokeNoReply("Speak", new Variant.VARIANT(text), new Variant.VARIANT(0));
        }

        public void waitUntilDone() {
            invokeNoReply("WaitUntilDone", new Variant.VARIANT(-1));
        }
    }

    public static void speak(@Nonnull String text, int rate) {
        if (text.isBlank()) {
            return;
        }

        SpVoice voice = getSpVoice();
        voice.setRate(rate);
        voice.speak(text);
        voice.waitUntilDone();
    }

    private static SpVoice getSpVoice() {
        if (spVoice == null) {
            Ole32.INSTANCE.CoInitializeEx(null, Ole32.COINIT_MULTITHREADED);
            spVoice = new SpVoice();
        }
        return spVoice;
    }
}
