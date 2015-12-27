package org.trypticon.talker.speech.robokoe.text;

import java.util.*;

/**
 * Holds a phoneme with its duration.
 */
class MaryDuration {
    private final float duration;
    private final String phoneme;

    private MaryDuration(float duration, String phoneme) {
        this.duration = duration;
        this.phoneme = phoneme;
    }

    float getDuration() {
        return duration;
    }

    String getPhoneme() {
        return phoneme;
    }

    static List<MaryDuration> fromLines(String text) {
        Iterator<String> iterator = Arrays.asList(text.split("\\n")).iterator();
        while (iterator.hasNext()) {
            String line = iterator.next();
            if (line.startsWith("#")) {
                break; // end of header
            }
        }
        List<MaryDuration> results = new LinkedList<>();
        while (iterator.hasNext()) {
            String line = iterator.next();
            line = line.trim();
            if (!line.isEmpty()) {
                String[] fields = line.split("\\s", 3);

                // fields[1] always contains 125 and apparently means nothing anymore.
                // http://www.dfki.de/pipermail/mary-users/2013-April/001312.html

                results.add(new MaryDuration(
                        Float.parseFloat(fields[0]),
                        fields[2]));
            }
        }
        return results;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "%s[%f, %s]", getClass().getSimpleName(), duration, phoneme);
    }
}
