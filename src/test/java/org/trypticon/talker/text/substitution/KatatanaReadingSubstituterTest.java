package org.trypticon.talker.text.substitution;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.trypticon.talker.model.Graph;
import org.trypticon.talker.text.Text;
import org.trypticon.talker.text.Token;
import org.trypticon.talker.text.TokenType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests for {@link KatakanaReadingSubstituterNode}.
 */
public class KatatanaReadingSubstituterTest {

    private KatakanaReadingSubstituterNode substituter;

    @BeforeEach
    public void setUp() {
        substituter = new KatakanaReadingSubstituterNode(new Graph(), "substituter_katakana_reading");
    }

    @Test
    public void testAlreadyJapanese() {
        String result = substituter.substitute(new Text(Collections.singletonList(
                new Token("テスト", TokenType.JAPANESE)))).getContent();

        assertThat(result, is("テスト"));
    }

    @Test
    public void testEnglish() {
        assertThat(substitute("test"), is("テスト"));
    }

    @Test
    public void testEnglish_ShortVowel() {
        assertThat(substitute("kit"), is("キト"));
    }

    @Test
    public void testEnglish_Hyphen() {
        assertThat(substitute("gill-bearing"), is("ギルベリン"));
    }

    @Test
    public void testEnglish_Schwas() {
        // akueriia would be fine too.
        // akueria would be better but depends on the dictionary having it.
        assertThat(substitute("aquaria"), is("アクウェリイア"));
    }

    @Test
    public void testEnglish_AdjoiningConsonants() {
        assertThat(substitute("standby"), is("スタンドバイ"));
    }

    @Test
    public void testEnglish_Si() {
        assertThat(substitute("consistently"), is("カンシスタントリイ"));
    }

    @Test
    public void testEnglish_To() {
        // toxu would be better, but requires vowel length work
        assertThat(substitute("to"), is("トゥウ"));
    }

    @Test
    public void testEnglish_Do() {
        // doxu would be better, but requires vowel length work
        assertThat(substitute("do"), is("ドゥウ"));
    }

    @Test
    public void testEnglish_This() {
        assertThat(substitute("this"), is("ジス"));
    }

    @Test
    public void testEnglish_Blur() {
        assertThat(substitute("blur"), is("ブラア"));
    }

    @Test
    public void testEnglish_World() {
        assertThat(substitute("world"), is("ワアルド"));
    }

    @Test
    public void testEnglish_Waves() {
        assertThat(substitute("waves"), is("ウェイヴズ"));
    }

    @Test
    public void testEnglish_Nobody() {
        assertThat(substitute("nobody"), is("ノウバディイ"));
    }

    @Test
    public void testEnglish_Voice() {
        assertThat(substitute("voice"), is("ヴォイス"));
    }

    @Test
    public void testEnglish_Evade() {
        assertThat(substitute("evade"), is("イヴェイド"));
    }

    @Test
    public void testEnglish_Voodoo() {
        assertThat(substitute("voodoo"), is("ヴウドゥウ"));
    }

    //todo difficult problem, marytts has weird pronunciation
//    @Test
//    public void testEnglish_America() {
//        assertThat(substitute("America"), is(equalTo("")));
//    }

    private String substitute(String input) {
        Text inputText = new Text(Collections.singletonList(
                new Token(input, TokenType.OTHER)));
        Text outputText = substituter.substitute(inputText);
        return outputText.getContent();
    }
}
