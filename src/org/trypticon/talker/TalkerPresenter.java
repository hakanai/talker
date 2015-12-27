package org.trypticon.talker;

import org.trypticon.talker.messages.*;
import org.trypticon.talker.speech.SpeechQueue;

import java.time.ZoneId;
import java.time.ZonedDateTime;
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
            Message message = event.getMessage();

            // Only speaks messages if they haven't been spoken before, to reduce annoyance.
            long lastSpokenMessage = preferences.getLong("lastSpokenMessage", 0);
            long thisMessageMillis = message.getTimestamp().toEpochMilli();
            if (thisMessageMillis > lastSpokenMessage) {
                speechQueue.post(message.getText());
                preferences.putLong("lastSpokenMessage", thisMessageMillis);
            }

            ZonedDateTime dateTime = message.getTimestamp().atZone(ZoneId.systemDefault());

            String dayDivider = String.format(
                    Locale.getDefault(Locale.Category.FORMAT),
                    "Day changed to %tF%n",
                    dateTime);
            if (!dayDivider.equals(lastDayDivider)) {
                view.appendText(dayDivider);
                lastDayDivider = dayDivider;
            }

            String messageLine = String.format(
                    Locale.getDefault(Locale.Category.FORMAT),
                    "%tR <%s> %s%n",
                    dateTime,
                    message.getSpeaker(),
                    message.getText());
            view.appendText(messageLine);

        }
    }
}
