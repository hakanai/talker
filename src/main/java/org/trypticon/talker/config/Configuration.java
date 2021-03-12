package org.trypticon.talker.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Configuration holder.
 */
public class Configuration {
    private final JsonObject configObject;

    private Configuration(JsonObject configObject) {
        this.configObject = configObject;
    }

    /**
     * Reads the configuration from a file.
     *
     * @param file the file to read from.
     * @return the configuration.
     * @throws IOException if an error occurs.
     */
    public static Configuration readFromFile(Path file) throws IOException {
        JsonObject configObject;
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            configObject = JsonParser.parseReader(reader).getAsJsonObject();
        }
        return new Configuration(configObject);
    }

    @Nonnull
    private JsonElement getRequiredElement(String key) {
        JsonElement element = configObject.get(key);
        if (element == null) {
            throw new IllegalArgumentException("Required config value was not set: " + key);
        }
        return element;
    }

    @Nonnull
    private Optional<JsonElement> getOptionalElement(String key) {
        return Optional.ofNullable(configObject.get(key));
    }

    public String getRequiredString(String key) {
        return getRequiredElement(key).getAsString();
    }

    public String getOptionalString(String key, String defaultValue) {
        return getOptionalElement(key)
                .map(JsonElement::getAsString)
                .orElse(defaultValue);
    }

    public int getRequiredInt(String key) {
        return getRequiredElement(key).getAsInt();
    }

    public int getOptionalInt(String key, int defaultValue) {
        return getOptionalElement(key)
                .map(JsonElement::getAsInt)
                .orElse(defaultValue);
    }

    public boolean getOptionalBoolean(String key, boolean defaultValue) {
        return getOptionalElement(key)
                .map(JsonElement::getAsBoolean)
                .orElse(defaultValue);
    }

    public Configuration getSubSection(String key) {
        return new Configuration(configObject.get(key).getAsJsonObject());
    }

    public List<Configuration> getSubSectionList(String key) {
        JsonArray array = configObject.get(key).getAsJsonArray();
        List<Configuration> results = new ArrayList<>(array.size());
        for (JsonElement element : array) {
            results.add(new Configuration(element.getAsJsonObject()));
        }
        return results;
    }
}
