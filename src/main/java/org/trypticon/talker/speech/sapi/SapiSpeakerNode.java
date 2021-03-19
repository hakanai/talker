package org.trypticon.talker.speech.sapi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import com.google.common.base.Strings;
import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.model.GraphLocation;
import org.trypticon.talker.speech.SpeakerNode;
import org.trypticon.talker.speech.util.ProcessUtils;
import org.trypticon.talker.text.Text;

/**
 * Speaker using Microsoft's Speech API (SAPI).
 */
public class SapiSpeakerNode extends SpeakerNode {
    private final String executable;
    private final String voice;
    private final int rate;
    private final boolean force32Bit;

    public SapiSpeakerNode(GraphLocation graphLocation, String providerId, Configuration configuration) {
        super(graphLocation, providerId, "Speaker: SAPI");

        voice = configuration.getOptionalString("voice").orElse(null);
        rate = configuration.getOptionalInt("rate").orElse(0);
        force32Bit = configuration.getOptionalBoolean("force32Bit").orElse(false);

        if (force32Bit) {
            // TODO: Potential for some JNA use here.
            //       Shell32Util.getKnownFolderPath(KnownFolders.FOLDERID_System)
            //       Shell32Util.getKnownFolderPath(KnownFolders.FOLDERID_SystemX86)
            executable = System.getenv("WINDIR") + "/SysWOW64/cscript.exe";
        } else {
            executable = "cscript.exe";
        }
    }

    @Override
    public void populateConfiguration(Configuration.Builder builder) {
        builder.putIfNotNull("voice", voice)
                .put("rate", rate)
                .put("force32Bit", force32Bit);
    }

    @Override
    public void speak(Text text) {
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

        // XXX: Talking to COM directly would be nicer than what we're currently doing here.

        StringBuilder script = new StringBuilder(
                "var VoiceObj = new ActiveXObject(\"Sapi.SpVoice\");\n" +
                "var AvailableVoices = VoiceObj.GetVoices();\n" +
                "var found = false;\n");
        if (voice == null) {
            script.append("for (var i = 0; i < AvailableVoices.Count; i++)\n" +
                    "{\n" +
                    "    found = true;\n" +
                    "    VoiceObj.Voice = AvailableVoices.Item(i);\n" +
                    "}\n");
        } else {
            script.append("for (var i = 0; i < AvailableVoices.Count; i++)\n" +
                            "{\n" +
                            "    var name = AvailableVoices.Item(i).GetDescription();\n" +
                            "    if (name == \"").append(escapeJavaScript(voice)).append("\")\n" +
                            "    {\n" +
                            "        found = true;\n" +
                            "        VoiceObj.Voice = AvailableVoices.Item(i);\n" +
                            "    }\n" +
                            "}\n");
        }
        script.append("if (found) {\n");
        if (rate != 0) {
            script.append("    VoiceObj.Rate = ").append(rate).append(";\n");
        }
        script.append(
                "    VoiceObj.Speak(\"").append(escapeJavaScript(text)).append("\");\n" +
                "} else {\n" +
                "    WScript.Quit(1);\n" +
                "}\n");
        Files.write(file, Collections.singletonList(script));
    }

    private String escapeJavaScript(String string) {

        // XXX: Guava doesn't have a JavaScript escaper yet:
        //      https://github.com/google/guava/issues/1620
        //      We could try to use Escapers to build one ourselves?

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
                        hex = Strings.padStart(hex, 4, '0');
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
