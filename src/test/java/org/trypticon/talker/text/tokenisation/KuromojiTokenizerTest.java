package org.trypticon.talker.text.tokenisation;

import java.net.URL;
import java.util.Arrays;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.trypticon.talker.text.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link KuromojiTokenizer}.
 */
public class KuromojiTokenizerTest {
    private KuromojiTokenizer tokeniser;

    @BeforeEach
    public void setUp() {
        tokeniser = new KuromojiTokenizer();
    }

    @Test
    public void testEnglish() {
        assertThat(tokeniser.tokenize("This is a test."), contains(Arrays.asList(
                token("This", TokenType.OTHER),
                token(" ", TokenType.WHITESPACE),
                token("is", TokenType.OTHER),
                token(" ", TokenType.WHITESPACE),
                token("a", TokenType.OTHER),
                token(" ", TokenType.WHITESPACE),
                token("test", TokenType.OTHER),
                token(".", TokenType.PUNCTUATION))));
    }

    @Test
    public void testJapanese() {
        assertThat(tokeniser.tokenize("これはテストです。"), contains(Arrays.asList(
                token("これ", TokenType.JAPANESE),
                token("は", TokenType.JAPANESE),
                token("テスト", TokenType.JAPANESE),
                token("です", TokenType.JAPANESE),
                token("。", TokenType.PUNCTUATION))));
    }

    @Test
    public void testJapaneseInEnglish() {
        assertThat(tokeniser.tokenize("His name is written as 山田太郎."), contains(Arrays.asList(
                token("His", TokenType.OTHER),
                token(" ", TokenType.WHITESPACE),
                token("name", TokenType.OTHER),
                token(" ", TokenType.WHITESPACE),
                token("is", TokenType.OTHER),
                token(" ", TokenType.WHITESPACE),
                token("written", TokenType.OTHER),
                token(" ", TokenType.WHITESPACE),
                token("as", TokenType.OTHER),
                token(" ", TokenType.WHITESPACE),
                token("山田", TokenType.JAPANESE),
                token("太郎", TokenType.JAPANESE),
                token(".", TokenType.PUNCTUATION))));
    }

    @Test
    public void testEnglishInJapanese() {
        assertThat(tokeniser.tokenize("初音ミクはVOCALOIDである。"), contains(Arrays.asList(
                token("初音", TokenType.JAPANESE),
                token("ミク", TokenType.JAPANESE),
                token("は", TokenType.JAPANESE),
                token("VOCALOID", TokenType.OTHER),
                token("で", TokenType.JAPANESE),
                token("ある", TokenType.JAPANESE),
                token("。", TokenType.PUNCTUATION))));
    }

    @Test
    public void testSingleW() {
        assertThat(tokeniser.tokenize("やめろｗ"), contains(Arrays.asList(
                token("やめろ", TokenType.JAPANESE),
                token("ｗ", TokenType.OTHER))));
    }

    @Test
    public void testMultipleW() {
        assertThat(tokeniser.tokenize("やめろｗｗｗｗｗｗ"), contains(Arrays.asList(
                token("やめろ", TokenType.JAPANESE),
                token("ｗｗｗｗｗｗ", TokenType.OTHER))));
    }

    @Test
    public void testRichText() throws Exception {
        Text richText = new Text(
                new TextToken("やめろｗｗｗｗｗｗ", TokenType.UNRECOGNISED),
                new Emoticon(new URL("http://example.com/emoticons/laugh.gif")));
        assertThat(tokeniser.tokenize(richText), contains(Arrays.asList(
                token("やめろ", TokenType.JAPANESE),
                token("ｗｗｗｗｗｗ", TokenType.OTHER),
                token("\uFFFC", TokenType.EMOTICON))));
    }

    private Matcher<Token> token(String text, TokenType type) {
        return allOf(
                instanceOf(Token.class),
                hasProperty("content", equalTo(text)),
                hasProperty("type", equalTo(type)));
    }
}
