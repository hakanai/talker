package org.trypticon.talker.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.google.gson.*;

/**
 * Configuration holder. _Essentially_ treated as immutable, though what it
 * _contains_ is currently not.
 */
@Immutable
public final class Configuration {
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

    /**
     * Writes the configuration to a file.
     *
     * @param file the file to write to.
     * @throws IOException if an error occurs.
     */
    public void writeToFile(Path file) throws IOException {
        // Write to a temp file to get more atomic behaviour.
        Path tempFile = file.resolveSibling(file.getFileName() + ".tmp");
        try (BufferedWriter writer = Files.newBufferedWriter(tempFile)) {
            new GsonBuilder()
                    .setPrettyPrinting()
                    .create()
                    .toJson(configObject, writer);
        }
    }

    /**
     * Gets a string from the configuration.
     *
     * @param key the key to look up.
     * @return the value assigned to the key.
     * @throws IllegalArgumentException if the string is missing from the configuration.
     */
    @Nonnull
    public String getRequiredString(String key) {
        return getOptionalString(key)
                .orElseThrow(() -> new IllegalArgumentException("Missing value for key: " + key));
    }

    /**
     * Gets a string from the configuration.
     *
     * @param key the key to look up.
     * @return the optional value assigned to the key.
     */
    @Nonnull
    public Optional<String> getOptionalString(String key) {
        return getOptionalElement(key)
                .map(JsonElement::getAsString);
    }

    /**
     * Gets an int from the configuration.
     *
     * @param key the key to look up.
     * @return the value assigned to the key.
     * @throws IllegalArgumentException if the string is missing from the configuration.
     */
    public int getRequiredInt(String key) {
        return getOptionalInt(key)
                .orElseThrow(() -> new IllegalArgumentException("Missing value for key: " + key));
    }

    /**
     * Gets an int from the configuration.
     *
     * @param key the key to look up.
     * @return the optional value assigned to the key.
     */
    @Nonnull
    public Optional<Integer> getOptionalInt(String key) {
        return getOptionalElement(key)
                .map(JsonElement::getAsInt);
    }

    /**
     * Gets a boolean from the configuration.
     *
     * @param key the key to look up.
     * @return the value assigned to the key.
     * @throws IllegalArgumentException if the string is missing from the configuration.
     */
    public boolean getRequiredBoolean(String key) {
        return getOptionalBoolean(key)
                .orElseThrow(() -> new IllegalArgumentException("Missing value for key: " + key));
    }

    /**
     * Gets a boolean from the configuration.
     *
     * @param key the key to look up.
     * @return the optional value assigned to the key.
     */
    @Nonnull
    public Optional<Boolean> getOptionalBoolean(String key) {
        return getOptionalElement(key)
                .map(JsonElement::getAsBoolean);
    }

    /**
     * Gets a sub-section from the configuration.
     *
     * @param key the key to look up.
     * @return a configuration object for the sub-section.
     */
    public Configuration getSubSection(String key) {
        return new Configuration(configObject.get(key).getAsJsonObject());
    }

    /**
     * Gets multiple sub-sections from the configuration.
     *
     * @param key the key to look up.
     * @return a list of configuration objects, one for each sub-section.
     */
    public List<Configuration> getSubSectionList(String key) {
        JsonArray array = configObject.get(key).getAsJsonArray();
        List<Configuration> results = new ArrayList<>(array.size());
        for (JsonElement element : array) {
            results.add(new Configuration(element.getAsJsonObject()));
        }
        return results;
    }


    @Nonnull
    private Optional<JsonElement> getOptionalElement(String key) {
        return Optional.ofNullable(configObject.get(key));
    }

    /**
     * Creates a new builder for building configuration objects.
     *
     * @return the builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for building configuration objects.
     */
    public static final class Builder {
        private final JsonObject configObject = new JsonObject();

        private Builder() {
        }

        /**
         * Puts a string value into the configuration.
         *
         * @param key the key to assign the value to.
         * @param value the value.
         * @return the builder, for chaining.
         */
        public Builder put(String key, @Nonnull String value) {
            configObject.addProperty(key, value);
            return this;
        }

        /**
         * Puts a string value into the configuration if the value is not null.
         *
         * @param key the key to assign the value to.
         * @param value the value.
         * @return the builder, for chaining.
         */
        public Builder putIfNotNull(String key, @Nullable String value) {
            if (value != null) {
                return put(key, value);
            }
            return this;
        }

        /**
         * Puts an int value into the configuration.
         *
         * @param key the key to assign the value to.
         * @param value the value.
         * @return the builder, for chaining.
         */
        public Builder put(String key, int value) {
            configObject.addProperty(key, value);
            return this;
        }

        /**
         * Puts a boolean value into the configuration.
         *
         * @param key the key to assign the value to.
         * @param value the value.
         * @return the builder, for chaining.
         */
        public Builder put(String key, boolean value) {
            configObject.addProperty(key, value);
            return this;
        }

        /**
         * Puts a sub-section into the configuration.
         *
         * @param key the key to assign the value to.
         * @param subSection the sub-section.
         * @return the builder, for chaining.
         */
        public Builder put(String key, Configuration subSection) {
            configObject.add(key, subSection.configObject);
            return this;
        }

        /**
         * Puts a list of sub-sections into the configuration.
         *
         * @param key the key to assign the value to.
         * @param subSections the sub-sections.
         * @return the builder, for chaining.
         */
        public Builder put(String key, List<Configuration> subSections) {
            JsonArray array = new JsonArray();
            for (Configuration subSection : subSections) {
                array.add(subSection.configObject);
            }
            configObject.add(key, array);
            return this;
        }

        /**
         * Builds the configuration.
         *
         * @return the configuration.
         */
        public Configuration build() {
            return new Configuration(configObject);
        }
    }
}
