package org.trypticon.talker.messages.twitch;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import javax.annotation.Nonnull;
import javax.imageio.ImageIO;

import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class TwitchEmotes {
    private static final LoadingCache<Integer, Image> cache = CacheBuilder.newBuilder().build(new CacheLoader<>() {
        @Override
        public Image load(@Nonnull Integer emoteId) throws IOException {
            return ImageIO.read(getEmoteURL(emoteId));
        }
    });

    private TwitchEmotes() {
    }

    public static URI getEmoteURI(int emoteId) {
        return URI.create("https://static-cdn.jtvnw.net/emoticons/v1/" + emoteId + "/1.0");
    }

    public static URL getEmoteURL(int emoteId) {
        try {
            return getEmoteURI(emoteId).toURL();
        } catch (IOException e) {
            throw new AssertionError("You can see for yourself that the URL will be valid", e);
        }
    }

    public static Image getEmote(int emoteId) throws IOException {
        try {
            return cache.get(emoteId);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            Throwables.propagateIfPossible(cause, IOException.class);
            throw new IllegalStateException(e);
        }
    }
}
