package talker.messages.ustream;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import talker.messages.AbstractMessageStream;
import talker.messages.Message;

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

/**
 * Reads the UStream social stream.
 */
public class UStreamMessageStream extends AbstractMessageStream {
    private static final Pattern cleanTextRegex = Pattern.compile("^(.*)\\s*\\(.*live at http://ustre.am/[0-9a-zA-Z]+\\)$");

    private final String preferenceSubKey;
    private final String baseUrl;
    private Timer timer;
    private final JsonParser jsonParser = new JsonParser();

    private int nextRefreshInterval;
    private long nextRangeStart;

    public UStreamMessageStream(int channelId) {
        preferenceSubKey = "ustream/" + channelId;
        baseUrl = "http://socialstream.ustream.tv/socialstream/get.json/" + channelId;
    }

    @Override
    public String getPreferenceSubKey() {
        return preferenceSubKey;
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
        fireRefreshStarted();

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

            JsonObject response = jsonParser.parse(reader).getAsJsonObject();

            JsonArray payload = response.get("payload").getAsJsonArray();
            int size = payload.size();
            for (int i = size - 1; i >= 0; i--) { // Processing in time order
                JsonObject itemObject = payload.get(i).getAsJsonObject();

                Instant timestamp = Instant.ofEpochSecond(itemObject.get("createdAt").getAsLong());
                String speaker = itemObject.get("displayName").getAsString().trim();
                URL speakerIcon = new URL(itemObject.get("profilePictureUrl").getAsString().trim());
                String text = itemObject.get("text").getAsString().trim();

                // Chop repetitive junk off the end. Somehow ustream itself doesn't show this stuff.
                Matcher matcher = cleanTextRegex.matcher(text);
                if (matcher.matches()) {
                    text = matcher.group(1);
                }

                fireMessageReceived(new Message(timestamp, speaker, speakerIcon, text));
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

            fireRefreshFinished();
        } catch (IOException e) {
            e.printStackTrace();

            //TODO: Indicate the type of error somehow...
            fireRefreshFailed();
        }
    }

}
