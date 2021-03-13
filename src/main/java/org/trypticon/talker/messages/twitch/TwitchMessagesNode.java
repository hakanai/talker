package org.trypticon.talker.messages.twitch;

import java.util.*;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.messages.Message;
import org.trypticon.talker.model.*;

/**
 * Reads the Twitch chat.
 */
public class TwitchMessagesNode extends Node implements Startable {
    private final String channel;
    private final OutputConnector messagesConnector;

    private TwitchClient client;

    public TwitchMessagesNode(Graph graph, String providerId, Configuration configuration) {
        super(graph, providerId, "Twitch Chat",
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

//  event.getMessageEvent().getTagValue("emotes")
            Map<String, String> tags = event.getMessageEvent().getTags();

            String name = tags.get("display-name");
            String message = event.getMessage();
            String emotes = tags.get("emotes");
            if (emotes != null) {
                message = stripEmotes(emotes, message);
            }

            sendOutput(messagesConnector, new Message(event.getFiredAtInstant(), name, null, message));
        });

        chat.joinChannel(channel);
    }

    @SuppressWarnings("UnstableApiUsage")
    private static String stripEmotes(String emotes, String message) {
        List<String> emoteList = Splitter.on('/').splitToList(emotes);
        RangeSet<Integer> ranges = TreeRangeSet.create();
        for (String emote : emoteList) {
            String rangesString = Splitter.on(':').limit(2).splitToList(emote).get(1);
            for (String rangeString : Splitter.on(',').split(rangesString)) {
                List<String> range = Splitter.on('-').limit(2).splitToList(rangeString);
                int startOffset = Integer.parseInt(range.get(0));
                int endOffset = Integer.parseInt(range.get(1));
                ranges.add(Range.closed(startOffset, endOffset));
            }
        }
        for (Range<Integer> range : ranges.asDescendingSetOfRanges()) {
            message = message.substring(0, range.lowerEndpoint()) + message.substring(range.upperEndpoint() + 1);
        }
        return message;
    }

    @Override
    public void stop() {
        if (client != null) {
            client.close();
        }
    }
}
