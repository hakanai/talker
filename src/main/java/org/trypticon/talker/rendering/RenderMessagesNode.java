package org.trypticon.talker.rendering;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import com.google.common.collect.ImmutableList;
import org.trypticon.talker.TalkerView;
import org.trypticon.talker.config.Configuration;
import org.trypticon.talker.messages.Message;
import org.trypticon.talker.model.ConnectorType;
import org.trypticon.talker.model.GraphLocation;
import org.trypticon.talker.model.InputConnector;
import org.trypticon.talker.model.Node;

public class RenderMessagesNode extends Node {
    private final TalkerView view;

    private final DateTimeFormatter dateFormatter;
    private final DateTimeFormatter timeFormatter;

    private String lastDayDivider;

    public RenderMessagesNode(GraphLocation graphLocation, String providerId, TalkerView view) {
        super(graphLocation, providerId, "Render Messages",
                ImmutableList.of(new InputConnector("messages", "Messages", ConnectorType.MESSAGE)),
                ImmutableList.of());

        this.view = view;

        dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
        timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM);
    }

    @Override
    public void populateConfiguration(Configuration.Builder builder) {
    }

    @Override
    public void push(InputConnector connector, Object data) {
        Message message = (Message) data;

        // XXX: Default time zone usage here. To avoid this we want some kind of context
        //      holding the time zone. Possibly the view itself?

        Locale locale = view.getLocale();
        ZonedDateTime dateTime = message.getTimestamp().atZone(ZoneId.systemDefault());
        String dateString = dateFormatter.withLocale(locale).format(dateTime);
        String timeString = timeFormatter.withLocale(locale).format(dateTime);

        String dayDivider = String.format(
                locale,
                "<table><tr><td><span color=\"808080\">Day changed to %s%n</span></td></tr></table>",
                dateString);
        if (!dayDivider.equals(lastDayDivider)) {
            view.appendMarkup(dayDivider);
            lastDayDivider = dayDivider;
        }

        String messageLine = String.format(
                locale,
                "<table><tr valign=\"top\"><td><img width=\"48\" height=\"48\" src=\"%s\"></td>" +
                        "<td><b>%s</b> <span color=\"#808080\">at %s</span><br>%s</td></tr></table>",
                message.getSpeakerIcon(),
                message.getSpeaker(),
                timeString,
                message.getText());
        view.appendMarkup(messageLine);
    }
}
