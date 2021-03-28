package org.trypticon.talker.speech.mac;

import javax.annotation.Nullable;
import javax.swing.*;

import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.config.Configurator;
import org.trypticon.talker.config.ConfiguratorUtils;
import org.trypticon.talker.model.GraphLocation;
import org.trypticon.talker.speech.SpeakerNode;
import org.trypticon.talker.speech.util.ProcessUtils;
import org.trypticon.talker.text.Text;

/**
 * Speaker using the 'say' command on OSX to do the speech.
 */
public class MacSpeakerNode extends SpeakerNode {
    @Nullable
    private final String voice;

    public MacSpeakerNode(GraphLocation graphLocation, String providerId, Configuration configuration) {
        super(graphLocation, providerId, "Speaker: macOS");

        voice = configuration.getOptionalString("voice").orElse(null);
    }

    @Override
    public void populateConfiguration(Configuration.Builder builder) {
        builder.putIfNotNull("voice", voice);
    }

    @Override
    public Configurator createConfigurator() {
        Configurator panel = new Configurator();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        // TODO: Should be localised
        JLabel label = new JLabel("Speaker:");
        JTextField speakerField = ConfiguratorUtils.createTextField();

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(label).addComponent(speakerField));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(label).addComponent(speakerField));

        return panel;
    }

    @Override
    public void speak(Text text) {
        if (voice == null) {
            ProcessUtils.execAndWait("say", text.getPlainTextContent());
        } else {
            ProcessUtils.execAndWait("say", "-v", voice, text.getPlainTextContent());
        }
    }
}
