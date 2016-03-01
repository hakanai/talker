package org.trypticon.talker.text.substitution;

import com.ibm.icu.text.Transliterator;
import org.trypticon.talker.text.Text;
import org.trypticon.talker.text.Token;
import org.trypticon.talker.text.TokenType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generates katakana readings for input text.
 */
class KatakanaGenerator {

    private final Map<String, PhonemeMapEntry> phonemeMap = new HashMap<>();
    {
        // V - str(u)t
        phonemeMap.put("V", new PhonemeMapEntry(Type.VOWEL, "a"));
        // @ - (a)ren(a)
        phonemeMap.put("@", new PhonemeMapEntry(Type.VOWEL, "a"));
        // r= - w(or)ld
        phonemeMap.put("r=", new PhonemeMapEntry(Type.VOWEL, "aa"));
        // A - f(a)ther
        phonemeMap.put("A", new PhonemeMapEntry(Type.VOWEL, "aa"));
        // { - tr(a)p
        //TODO: sometimes it's "ya", e.g. c(a)t -> k(ya)tto
        phonemeMap.put("{", new PhonemeMapEntry(Type.VOWEL, "a"));

        // I - k(i)t
        phonemeMap.put("I", new PhonemeMapEntry(Type.VOWEL, "i"));
        // i - b(e)
        phonemeMap.put("i", new PhonemeMapEntry(Type.VOWEL, "ii"));

        // U - f(oo)t
        phonemeMap.put("U", new PhonemeMapEntry(Type.VOWEL, "u"));
        // u - b(oo)m
        phonemeMap.put("u", new PhonemeMapEntry(Type.VOWEL, "uu"));

        // E - m(e)t
        phonemeMap.put("E", new PhonemeMapEntry(Type.VOWEL, "e"));

        // O - th(ou)ght
        phonemeMap.put("O", new PhonemeMapEntry(Type.VOWEL, "oo"));

        // aU - h(ow)
        phonemeMap.put("aU", new PhonemeMapEntry(Type.VOWEL, "au"));
        // OI - b(oy)
        phonemeMap.put("OI", new PhonemeMapEntry(Type.VOWEL, "oi"));
        // @U - l(ow)
        phonemeMap.put("@U", new PhonemeMapEntry(Type.VOWEL, "ou"));
        // EI - d(ay)
        phonemeMap.put("EI", new PhonemeMapEntry(Type.VOWEL, "ei"));
        // AI - dr(y)
        phonemeMap.put("AI", new PhonemeMapEntry(Type.VOWEL, "ai"));

        // p - s(p)eak
        phonemeMap.put("p", new PhonemeMapEntry(Type.CONSONANT, "p"));
        // t - s(t)ew
        phonemeMap.put("t", new PhonemeMapEntry(Type.CONSONANT, "t"));
        // k - s(c)at
        phonemeMap.put("k", new PhonemeMapEntry(Type.CONSONANT, "k"));
        // b - (b)ed
        phonemeMap.put("b", new PhonemeMapEntry(Type.CONSONANT, "b"));
        // d - (d)ig
        phonemeMap.put("d", new PhonemeMapEntry(Type.CONSONANT, "d"));
        // g - (g)ame
        phonemeMap.put("g", new PhonemeMapEntry(Type.CONSONANT, "g"));
        // tS - e(tch)
        phonemeMap.put("tS", new PhonemeMapEntry(Type.CONSONANT, "t"));
        // dZ - e(dge)
        phonemeMap.put("dZ", new PhonemeMapEntry(Type.CONSONANT, "j"));
        // f - (f)ive
        phonemeMap.put("f", new PhonemeMapEntry(Type.CONSONANT, "f"));
        // v - (v)est
        phonemeMap.put("v", new PhonemeMapEntry(Type.CONSONANT, "v"));
        // T - (th)ick
        phonemeMap.put("T", new PhonemeMapEntry(Type.CONSONANT, "s"));
        // D - (th)is
        phonemeMap.put("D", new PhonemeMapEntry(Type.CONSONANT, "z"));
        // s - (s)ing
        phonemeMap.put("s", new PhonemeMapEntry(Type.CONSONANT, "s"));
        // z - (z)ap
        phonemeMap.put("z", new PhonemeMapEntry(Type.CONSONANT, "z"));
        // S - fi(sh)
        phonemeMap.put("S", new PhonemeMapEntry(Type.CONSONANT, "sh"));
        // Z - lei(s)ure
        phonemeMap.put("Z", new PhonemeMapEntry(Type.CONSONANT, "j"));
        // h - (h)ouse
        phonemeMap.put("h", new PhonemeMapEntry(Type.CONSONANT, "h"));
        // l - (l)ove
        phonemeMap.put("l", new PhonemeMapEntry(Type.CONSONANT, "r"));
        // m - (m)oon
        phonemeMap.put("m", new PhonemeMapEntry(Type.CONSONANT, "m"));
        // n - (n)ote
        phonemeMap.put("n", new PhonemeMapEntry(Type.CONSONANT, "n"));
        // N - thi(ng)
        phonemeMap.put("N", new PhonemeMapEntry(Type.CONSONANT, "n"));
        // r - (r)ed
        phonemeMap.put("r", new PhonemeMapEntry(Type.CONSONANT, "r"));
        // w - (w)est
        phonemeMap.put("w", new PhonemeMapEntry(Type.CONSONANT, "w"));
        // j - (y)es
        phonemeMap.put("j", new PhonemeMapEntry(Type.CONSONANT, "i"));
    }

    private final MaryWrapper mary;
    private final Transliterator transliterator;

    KatakanaGenerator() {
        mary = new MaryWrapper();

        transliterator = Transliterator.getInstance("Latin-Katakana");
    }

    /**
     * Converts English text to Romaji.
     *
     * @param english the English.
     * @return the Romaji.
     */
    private String englishToRomaji(String english) {
        StringBuilder result = new StringBuilder();

        // Hacky special case to handle receiving a trailing period which occurred
        // immediately after something we didn't have to convert.
        Matcher matcher = Pattern.compile("^\\W+").matcher(english);
        if (matcher.find()) {
            result.append(matcher.group());
        }

        PhonemeMapEntry last = null;
        for (MaryDuration duration : mary.generate(english)) {
            String englishPhoneme = duration.getPhoneme();
            if ("_".equals(englishPhoneme)) {
                if (last != null && last.type == Type.CONSONANT) {
                    padVowel(result, "");
                }

                last = null;
                continue;
            }

            PhonemeMapEntry entry = phonemeMap.get(englishPhoneme);
            if (entry == null) {
                throw new IllegalStateException("No entry: " + englishPhoneme);
            }

            // Prevent multiple consonants landing in a row.
            if (last != null && last.type == Type.CONSONANT) {
                padVowel(result, entry.romaji);
            }

            result.append(entry.romaji);

            last = entry;
        }

        return result.toString();
    }

    /**
     * Manipulates the end of the string to follow rules for what can be converted into Katakana.
     *
     * @param builder the text being built up.
     * @param nextText the next text we're about to append.
     */
    private void padVowel(StringBuilder builder, String nextText) {
        char firstOfNext = nextText.isEmpty() ? '\0' : nextText.charAt(0);
        boolean nextIsConsonant = isConsonant(firstOfNext);
        switch (builder.charAt(builder.length() - 1)) {
            case 'm':
                // Special case to convert mb/mp/mm into nb/np/mm
                if (firstOfNext == 'b' || firstOfNext == 'p' || firstOfNext == 'm') {
                    builder.setCharAt(builder.length() - 1, 'n');
                } else if (nextIsConsonant) {
                    builder.append('u');
                }
                break;
            case 'n':
                break;
            case 't':
                // Special case to let through ts as-is. TODO: Probably still not perfect. :(
                if (firstOfNext != 's' && nextIsConsonant) {
                    builder.append('o');
                }
                break;
            case 'd':
                // Special case mirroring the one we had for t
                if (firstOfNext != 'z') {
                    builder.append('o');
                }
                break;
            case 's':
                // 'si' -> 'shi', otherwise you get 'sexi' which gets read out as just 'se'.
                if (firstOfNext == 'i') {
                    builder.append('h');
                } else if (nextIsConsonant) {
                    builder.append('u');
                }
                break;
            default:
                if (nextIsConsonant) {
                    builder.append('u'); //TODO: There are probably other cases I'm forgetting...
                }
        }
    }

    private boolean isConsonant(char ch) {
        switch (ch) {
            case 'a':
            case 'e':
            case 'i':
            case 'o':
            case 'u':
                return false;
            default:
                return true;
        }
    }

    /**
     * Converts English text to Katakana readings.
     *
     * @param english the English text.
     * @return the Katakana reading of the text.
     */
    Text englishToKatakana(Text english) {
        String romaji = englishToRomaji(english.getContent());
        return new Text(Collections.singletonList(new Token(
                romajiToKatakana(romaji),
                TokenType.JAPANESE)));
    }

    private String romajiToKatakana(String romaji) {
        return transliterator.transliterate(romaji)
                .replace("テゥ", "トゥ");
    }

    /**
     * Converts English punctuation to the Japanese equivalent.
     *
     * @param punctuation the English punctuation.
     * @return the Japanese equivalent punctuation.
     */
    Text punctuationToJapanese(Text punctuation) {
        return new Text(Collections.singletonList(new Token(
                transliterator.transliterate(punctuation.getContent()),
                TokenType.PUNCTUATION)));
    }

    private static class PhonemeMapEntry {
        private final Type type;
        private final String romaji;

        public PhonemeMapEntry(Type type, String romaji) {
            this.type = type;
            this.romaji = romaji;
        }
    }

    private enum Type {
        VOWEL, CONSONANT
    }
}
