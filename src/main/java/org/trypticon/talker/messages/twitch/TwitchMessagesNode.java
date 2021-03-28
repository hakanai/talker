package org.trypticon.talker.messages.twitch;

import java.util.List;
import java.util.Map;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import org.jetbrains.annotations.NotNull;
import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.messages.Message;
import org.trypticon.talker.model.*;
import org.trypticon.talker.text.*;

/**
 * Reads the Twitch chat.
 */
public class TwitchMessagesNode extends Node implements Startable {

    // Examples of the strings we have to split up:
    // 81274:41-46
    // 58765:34-44,46-56,58-68
    // 305954156:0-7/304027941:65-77
    private static final Splitter RANGE_SPLITTER = Splitter.on('-').limit(2);
    private static final Splitter RANGE_LIST_SPLITTER = Splitter.on(',');
    private static final Splitter EMOTE_ID_AND_RANGE_LIST_SPLITTER = Splitter.on(':').limit(2);
    private static final Splitter EMOTE_LIST_SPLITTER = Splitter.on('/');

    private final String channel;
    private final OutputConnector messagesConnector;

    private TwitchClient client;

    public TwitchMessagesNode(GraphLocation graphLocation, String providerId, Configuration configuration) {
        super(graphLocation, providerId, "Twitch Chat",
                ImmutableList.of(),
                ImmutableList.of(new OutputConnector("messages", "Messages", ConnectorType.MESSAGE)));

        channel = configuration.getRequiredString("channel");
        messagesConnector = getOutputConnectorById("messages");
    }

    @Override
    public void populateConfiguration(Configuration.Builder builder) {
        builder.put("channel", channel);
    }

    @Override
    public void push(InputConnector connector, Object data) {
    }

    @Override
    public void start() {
        client = TwitchClientBuilder.builder()
                .withEnableChat(true)
                .build();

        TwitchChat chat = client.getChat();

        chat.getEventManager().onEvent(ChannelMessageEvent.class, event -> {

            // XXX: Can get user icons but requires Helix API and therefore OAuth
            //      client.getHelix().getUsers().execute().getUsers()
            //            .get(0).getProfileImageUrl();

            Map<String, String> tags = event.getMessageEvent().getTags();

            System.out.println("======================================");
            System.out.println("Message:");
            System.out.println(event.getMessage());
            System.out.println("Tags:");
            System.out.println(tags);

            String name = tags.get("display-name");
            String message = event.getMessage();
            Text richText;
            String emotes = tags.get("emotes");
            if (emotes != null) {
                richText = parseEmotes(emotes, message);
            } else {
                richText = new Text(new TextToken(message, TokenType.UNRECOGNISED));
            }

            sendOutput(messagesConnector, new Message(event.getFiredAtInstant(), name, null, richText));
        });

        chat.joinChannel(channel);
    }

    @SuppressWarnings("UnstableApiUsage")
    private static Text parseEmotes(String emotes, String message) {
        RangeMap<Integer, Integer> ranges = TreeRangeMap.create();
        for (String emote : EMOTE_LIST_SPLITTER.split(emotes)) {
            List<String> emoteIdAndRangesString = EMOTE_ID_AND_RANGE_LIST_SPLITTER.splitToList(emote);
            int emoteId = Integer.parseInt(emoteIdAndRangesString.get(0));
            for (String rangeString : RANGE_LIST_SPLITTER.split(emoteIdAndRangesString.get(1))) {
                List<String> range = RANGE_SPLITTER.splitToList(rangeString);
                int startOffset = Integer.parseInt(range.get(0));
                int endOffset = Integer.parseInt(range.get(1));
                ranges.put(Range.closed(startOffset, endOffset), emoteId);
            }
        }

        int[] messageCodePoints = message.codePoints().toArray();

        ImmutableList.Builder<Token> tokens = ImmutableList.builder();
        int lastOffset = 0;
        for (Map.Entry<Range<Integer>, Integer> entry : ranges.asMapOfRanges().entrySet()) {
            Range<Integer> range = entry.getKey();
            Integer emoteId = entry.getValue();

            if (range.lowerEndpoint() > lastOffset) {
                tokens.add(createTextToken(messageCodePoints, lastOffset, range.lowerEndpoint()));
            }

            tokens.add(new Emoticon(TwitchEmotes.getEmoteURL(emoteId)));

            lastOffset = range.upperEndpoint() + 1;
        }

        if (lastOffset < messageCodePoints.length) {
            tokens.add(createTextToken(messageCodePoints, lastOffset, messageCodePoints.length));
        }

        return new Text(tokens.build());
    }

    @NotNull
    private static TextToken createTextToken(int[] messageCodePoints, int startOffset, int endOffset) {
        return new TextToken(new String(messageCodePoints,
                startOffset, endOffset - startOffset), TokenType.UNRECOGNISED);
    }

    @Override
    public void stop() {
        if (client != null) {
            client.close();
        }
    }
}
