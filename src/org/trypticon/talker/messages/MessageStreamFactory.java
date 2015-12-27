package org.trypticon.talker.messages;

import org.trypticon.talker.messages.ustream.UStreamMessageStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Responsible for creating a message stream.
 */
public class MessageStreamFactory {
    public MessageStream create() {
        Properties properties = new Properties();
        try (InputStream stream = new BufferedInputStream(Files.newInputStream(Paths.get("config.properties")))) {
            properties.load(stream);
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't load config.properties", e);
        }

        int channelId = Integer.parseInt(properties.getProperty("messages.source.channelId"));

        return new UStreamMessageStream(channelId);
    }
}
