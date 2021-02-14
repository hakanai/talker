package org.trypticon.talker.text.tokenisation;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.trypticon.talker.text.Token;
import org.trypticon.talker.text.TokenType;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link KuromojiTokeniser}.
 */
public class KuromojiTokeniserTest {
    private KuromojiTokeniser tokeniser;

    @BeforeEach
    public void setUp() {
        tokeniser = new KuromojiTokeniser();
    }

    @Test
    public void testEnglish() {
        assertThat(tokeniser.tokenise("This is a test."), contains(Arrays.asList(
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
        assertThat(tokeniser.tokenise("これはテストです。"), contains(Arrays.asList(
                token("これ", TokenType.JAPANESE),
                token("は", TokenType.JAPANESE),
                token("テスト", TokenType.JAPANESE),
                token("です", TokenType.JAPANESE),
                token("。", TokenType.PUNCTUATION))));
    }

    @Test
    public void testJapaneseInEnglish() {
        assertThat(tokeniser.tokenise("His name is written as 山田太郎."), contains(Arrays.asList(
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
        assertThat(tokeniser.tokenise("初音ミクはVOCALOIDである。"), contains(Arrays.asList(
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
        assertThat(tokeniser.tokenise("やめろｗ"), contains(Arrays.asList(
                token("やめろ", TokenType.JAPANESE),
                token("ｗ", TokenType.OTHER))));
    }

    @Test
    public void testMultipleW() {
        assertThat(tokeniser.tokenise("やめろｗｗｗｗｗｗ"), contains(Arrays.asList(
                token("やめろ", TokenType.JAPANESE),
                token("ｗｗｗｗｗｗ", TokenType.OTHER))));
    }

    private Matcher<Token> token(String text, TokenType type) {
        return allOf(
                instanceOf(Token.class),
                hasProperty("content", equalTo(text)),
                hasProperty("type", equalTo(type)));
    }
}
