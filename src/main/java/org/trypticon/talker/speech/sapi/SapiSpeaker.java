package org.trypticon.talker.speech.sapi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.speech.Speaker;
import org.trypticon.talker.speech.util.ProcessUtils;
import org.trypticon.talker.text.Text;
import org.trypticon.talker.text.substitution.KatakanaReadingSubstituter;

/**
 * Speaker using Microsoft's Speech API (SAPI).
 */
public class SapiSpeaker implements Speaker {
    private final String executable;
    private final String voice;
    private final int rate;

    private final KatakanaReadingSubstituter readingSubstituter = new KatakanaReadingSubstituter();

    public SapiSpeaker(String voice, int rate, boolean force32Bit) {
        if (force32Bit) {
            executable = System.getenv("WINDIR") + "/SysWOW64/cscript.exe";
        } else {
            executable = "cscript.exe";
        }

        this.voice = voice;
        this.rate = rate;
    }

    public SapiSpeaker(Configuration configuration) {
        this(configuration.getString("voice"),
                configuration.getInt("rate"),
                configuration.getBoolean("force32Bit"));
    }

    @Override
    public void speak(Text text) {

        text = readingSubstituter.substitute(text);

        Path tempFile = null;
        try {
            tempFile = Files.createTempFile("speak", ".js");
            writeScript(tempFile, text.getContent());
            ProcessUtils.execAndWait(executable, "/nologo", tempFile.toString());
        } catch (IOException e) {
            throw new IllegalStateException("Error writing temp files?", e);
        } finally {
            if (tempFile != null) {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException e) {
                    // Probably fine. I guess another error occurred and that will get reported.
                }
            }
        }
    }

    private void writeScript(Path file, String text) throws IOException {
        String escapedVoice = escape(voice);
        String escapedText = escape(text);
        String script =
                "var VoiceObj = new ActiveXObject(\"Sapi.SpVoice\");\n" +
                "var AvailableVoices = VoiceObj.GetVoices();\n" +
                "var found = false;\n" +
                "for (var i = 0; i< AvailableVoices.Count; i++)\n" +
                "{\n" +
                "    var name = AvailableVoices.Item(i).GetDescription();\n" +
                "    if (name == \"" + escapedVoice + "\")\n" +
                "    {\n" +
                "        found = true;\n" +
                "        VoiceObj.Voice = AvailableVoices.Item(i);\n" +
                "    }\n" +
                "}\n" +
                "if ( found ) {\n" +
                "    VoiceObj.Rate = " + rate + ";\n" +
                "    VoiceObj.Speak(\"" + escapedText + "\");\n" +
                "} else {\n" +
                "    WScript.Quit(1);\n" +
                "}\n";
        Files.write(file, Collections.singletonList(script));
    }

    private String escape(String string) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            switch (ch) {
                case '\\':
                case '\"':
                case '\n':
                    result.append('\\').append(ch);
                    break;
                default: {
                    // Escape Unicode even though we shouldn't have to, because we can't trust MS
                    // to use a sensible encoding to read the file.
                    if (ch > 0x7f) {
                        String hex = Integer.toString(ch, 16);
                        while (hex.length() < 4) {
                            hex = '0' + hex;
                        }
                        result.append("\\u").append(hex);
                    } else {
                        result.append(ch);
                    }
                }
            }
        }
        return result.toString();
    }
}
