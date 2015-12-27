package org.trypticon.talker;

import org.trypticon.talker.messages.*;
import org.trypticon.talker.speech.SpeechQueue;

import javax.swing.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.prefs.Preferences;

/**
 * Coordinates messages, the display and the speech queue.
 */
class TalkerPresenter {
    private final SpeechQueue speechQueue = new SpeechQueue();
    private final TalkerView view;
    private MessageStream stream;
    private final MessageStreamListener messageStreamListener = new MessageStreamHandler();

    private Preferences preferences;
    private String lastDayDivider;

    TalkerPresenter(TalkerView view) {
        this.view = view;
    }

    public void start() {
        speechQueue.start();
        refreshStream();
    }

    public void stop() {
        stopStream();
        speechQueue.stop();
    }

    private void stopStream() {
        if (stream != null) {
            stream.stop();
            stream.removeMessageStreamListener(messageStreamListener);
            stream = null;
        }
    }

    private void refreshStream() {
        stopStream();

        MessageStream stream = new MessageStreamFactory().create();
        stream.addMessageStreamListener(messageStreamListener);
        preferences = Preferences.userRoot().node("org/trypticon/talker/messages/" + stream.getPreferenceSubKey());

        this.stream = stream;
        stream.start();
    }

    private class MessageStreamHandler implements MessageStreamListener {
        @Override
        public void messageReceived(MessageStreamEvent event) {
            SwingUtilities.invokeLater(() -> {
                Message message = event.getMessage();

                // Only speaks messages if they haven't been spoken before, to reduce annoyance.
                long lastSpokenMessage = preferences.getLong("lastSpokenMessage", 0);
                long thisMessageMillis = message.getTimestamp().toEpochMilli();
                if (thisMessageMillis > lastSpokenMessage) {
                    speechQueue.post(message.getText());
                    preferences.putLong("lastSpokenMessage", thisMessageMillis);
                }

                Locale locale = Locale.getDefault(Locale.Category.FORMAT);
                ZonedDateTime dateTime = message.getTimestamp().atZone(ZoneId.systemDefault());
                String dateString = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(dateTime);
                String timeString = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(dateTime);

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
            });
        }

        @Override
        public void refreshStarted(MessageStreamEvent event) {
            SwingUtilities.invokeLater(() -> view.updateStatus("Refreshing..."));
        }

        @Override
        public void refreshFinished(MessageStreamEvent event) {
            SwingUtilities.invokeLater(() -> view.updateStatus(" "));
        }

        @Override
        public void refreshFailed(MessageStreamEvent event) {
            view.updateStatus("Refresh failed.");
            SwingUtilities.invokeLater(() -> view.updateStatus("Refresh failed."));
        }
    }
}
