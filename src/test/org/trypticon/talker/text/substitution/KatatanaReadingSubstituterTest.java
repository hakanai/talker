package org.trypticon.talker.text.substitution;

import org.junit.Test;
import org.trypticon.talker.speech.robokoe.RoboKoeSpeaker;
import org.trypticon.talker.speech.sapi.SapiSpeaker;
import org.trypticon.talker.text.Text;
import org.trypticon.talker.text.Token;
import org.trypticon.talker.text.TokenType;

import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link KatakanaReadingSubstituter}.
 */
public class KatatanaReadingSubstituterTest {

    @Test
    public void testAlreadyJapanese() {
        String result = new KatakanaReadingSubstituter().substitute(new Text(Collections.singletonList(
                new Token("テスト", TokenType.JAPANESE)))).getContent();

        assertThat(result, is(equalTo("テスト")));
    }

    @Test
    public void testEnglish() {
        assertThat(substitute("test"), is(equalTo("テスト")));
    }

    @Test
    public void testEnglish_ShortVowel() {
        assertThat(substitute("kit"), is(equalTo("キト")));
    }

    @Test
    public void testEnglish_Hyphen() {
        assertThat(substitute("gill-bearing"), is(equalTo("ギルベリン")));
    }

    @Test
    public void testEnglish_Schwas() {
        // akueriia would be fine too.
        // akueria would be better but depends on the dictionary having it.
        assertThat(substitute("aquaria"), is(equalTo("アクウェリイア")));
    }

    @Test
    public void testEnglish_AdjoiningConsonants() {
        assertThat(substitute("standby"), is(equalTo("スタンドバイ")));
    }

    @Test
    public void testEnglish_Si() {
        assertThat(substitute("consistently"), is(equalTo("カンシスタントリイ")));
    }

    @Test
    public void testEnglish_To() {
        // toxu would be better, but requires vowel length work
        assertThat(substitute("to"), is(equalTo("トゥウ")));
    }

    @Test
    public void testEnglish_Do() {
        // doxu would be better, but requires vowel length work
        assertThat(substitute("do"), is(equalTo("ドゥウ")));
    }

    @Test
    public void testEnglish_This() {
        assertThat(substitute("this"), is(equalTo("ジス")));
    }

    @Test
    public void testEnglish_Blur() {
        assertThat(substitute("blur"), is(equalTo("ブラア")));
    }

    @Test
    public void testEnglish_World() {
        assertThat(substitute("world"), is(equalTo("ワアルド")));
    }

    @Test
    public void testEnglish_Waves() {
        assertThat(substitute("waves"), is(equalTo("ウェイヴズ")));
    }

    @Test
    public void testEnglish_Nobody() {
        assertThat(substitute("nobody"), is(equalTo("ノウバディイ")));
    }

    private String substitute(String input) {
        Text inputText = new Text(Collections.singletonList(
                new Token(input, TokenType.OTHER)));
        Text outputText = new KatakanaReadingSubstituter().substitute(inputText);
        return outputText.getContent();
    }
}
