package org.trypticon.talker.messages.ustream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.messages.Message;
import org.trypticon.talker.model.*;

/**
 * Reads the UStream social stream.
 */
public class UStreamMessagesNode extends Node implements Startable {
    private static final Pattern cleanTextRegex = Pattern.compile("^(.*)\\s*\\(.*live at http://ustre.am/[0-9a-zA-Z]+\\)$");

    private final String preferenceSubKey;
    private final String baseUrl;

    private final OutputConnector messagesConnector;
    private final OutputConnector statusEventsConnector;

    private int channelId;
    private Timer timer;

    private int nextRefreshInterval;
    private long nextRangeStart;

    public UStreamMessagesNode(Graph graph, String providerId, Configuration configuration) {
        super(graph, providerId, "UStream Messages",
                ImmutableList.of(),
                ImmutableList.of(
                        new OutputConnector("messages", "Messages", ConnectorType.TEXT),
                        new OutputConnector("statusEvents", "Status Events", ConnectorType.TEXT)));

        channelId = configuration.getRequiredInt("channelId");

        messagesConnector = getOutputConnectors().get(0);
        statusEventsConnector = getOutputConnectors().get(1);

        preferenceSubKey = "ustream/" + channelId;
        baseUrl = "http://socialstream.ustream.tv/socialstream/get.json/" + channelId;
    }

    @Override
    public void populateConfiguration(Configuration.Builder builder) {
        builder.put("channelId", channelId);
    }

    public String getPreferenceSubKey() {
        return preferenceSubKey;
    }

    @Override
    public void push(InputConnector connector, Object data) {
        // No input
    }

    @Override
    public void start() {
        timer = new Timer(getClass().getSimpleName());
        timer.schedule(fetchNewMessagesTask(), 100);
    }

    @Override
    public void stop() {
        timer.cancel();
    }

    private TimerTask fetchNewMessagesTask() {
        return new TimerTask() {
            @Override
            public void run() {
                fetchNewMessages();
            }
        };
    }

    private void fetchNewMessages() {
        sendOutput(statusEventsConnector, newStatusMessage("Refresh Started"));

        URL url;
        try {
            if (nextRangeStart == 0) {
                url = new URL(baseUrl + "/default");
            } else {
                url = new URL(baseUrl + "/timeslice/" + nextRangeStart + '/' + nextRefreshInterval);
            }
        } catch (MalformedURLException e) {
            throw new IllegalStateException("URL was malformed but we created it programmatically", e);
        }

        try (InputStream stream = url.openStream();
             Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {

            JsonObject response = JsonParser.parseReader(reader).getAsJsonObject();

            JsonArray payload = response.get("payload").getAsJsonArray();
            int size = payload.size();
            for (int i = size - 1; i >= 0; i--) { // Processing in time order
                JsonObject itemObject = payload.get(i).getAsJsonObject();

                JsonElement typeElement = itemObject.get("type");
                if (typeElement != null) {
                    // Special message.
                    switch (typeElement.getAsString()) {
                        case "host":
                            // "host", "host":(new url)
                            break;

                        case "ignMsg":
                            // Ignore a previous message. In our case, it's too late, we already spoke it.
                            // "ignMsg", "uid":(message id)
                            break;

                        case "ignUser":
                            // "ignUser", "network":(user info)
                            break;

                        case "unIgnUser":
                            // "unIgnUser", "network":(user info)
                            break;
                    }
                } else {
                    // Normal message
                    Instant timestamp = Instant.ofEpochSecond(itemObject.get("createdAt").getAsLong());

                    /* TODO - Resurrect last spoken message tracking. nextRangeStart might work?
                    // TODO: Filtering already processed messages should perhaps be handled internally by the source
                    // Only speaks messages if they haven't been spoken before, to reduce annoyance.
                    long thisMessageMillis = message.getTimestamp().toEpochMilli();
                    if (thisMessageMillis > lastSpokenMessage) {
                        speechQueue.post(message.getText());
                        lastSpokenMessage = thisMessageMillis;
                        preferences.putLong("lastSpokenMessage", thisMessageMillis);
                    }
                    */

                    System.out.println(itemObject);
                    String speaker = itemObject.get("displayName").getAsString().trim();
                    URL speakerIcon = new URL(itemObject.get("profilePictureUrl").getAsString().trim());
                    String text = itemObject.get("text").getAsString().trim();

                    // Chop repetitive junk off the end. Somehow ustream itself doesn't show this stuff.
                    Matcher matcher = cleanTextRegex.matcher(text);
                    if (matcher.matches()) {
                        text = matcher.group(1);
                    }

                    sendOutput(messagesConnector, new Message(timestamp, speaker, speakerIcon, text));
                }
            }

            JsonArray range = response.get("range").getAsJsonArray();
            long rangeStart = range.get(0).getAsLong();
            long rangeEnd = range.get(1).getAsLong();

            nextRefreshInterval = response.get("refreshInterval").getAsInt();
            nextRangeStart = rangeEnd;

            long nextRequestTime = (rangeEnd + nextRefreshInterval) * 1000;
            //TODO: Their docs say to do something like this, but it results in nonsense values.
//            long nextRequestTime;
//            if (rangeStart == 0) {
//                nextRequestTime = (rangeEnd + nextRefreshInterval) * 1000;
//            } else {
//                nextRequestTime = (requestTime + (rangeEnd - rangeStart) + nextRefreshInterval) * 1000;
//            }

//            System.out.println("It is currently: " + new Date());
//            System.out.println("Will make next request at: " + new Date(nextRequestTime));
            timer.schedule(fetchNewMessagesTask(), new Date(nextRequestTime));

            sendOutput(statusEventsConnector, newStatusMessage("Refresh Finished"));
        } catch (IOException e) {
            e.printStackTrace();

            //TODO: Indicate the type of error somehow...
            sendOutput(statusEventsConnector, newStatusMessage("Refresh Failed"));

            //TODO: Some kind of exponential back-off?
            long now = System.currentTimeMillis();
            int retry = 10;

            if (nextRangeStart > 0) {
                nextRefreshInterval = (int) (now/1000 + retry - nextRangeStart);
            }

            long nextRequestTime = now + retry*1000;
            timer.schedule(fetchNewMessagesTask(), new Date(nextRequestTime));
        }
    }

    private static Message newStatusMessage(String text) {
        // TODO: Get some generic system icon?
        return new Message(Instant.now(), "UStream", null, text);
    }

}
