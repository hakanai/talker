package talker.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
        JsonParser jsonParser = new JsonParser();
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            configObject = jsonParser.parse(reader).getAsJsonObject();
        }
        return new Configuration(configObject);
    }

    public String getString(String key) {
        return configObject.get(key).getAsString();
    }

    public int getInt(String key) {
        return configObject.get(key).getAsInt();
    }

    public boolean getBoolean(String key) {
        return configObject.get(key).getAsBoolean();
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
