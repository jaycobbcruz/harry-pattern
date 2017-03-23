package org.alicebot.ab.client;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.client.BotFactory;

import java.util.concurrent.ExecutionException;

public class ChatSession {

    private org.alicebot.ab.client.BotFactory botFactory;

    public ChatSession(org.alicebot.ab.client.BotFactory botFactory) {
        this.botFactory = botFactory;
    }

    public Chat get(final String userId) {
        final LoadingCache<String, Chat> graphs = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .build(new CacheLoader<String, Chat>() {
                    public Chat load(String key) {
                        final Bot bot = botFactory.get(BotFactory.DEFAULT_BOT);
                        return new Chat(bot, key);
                    }
                });
        try {
            return graphs.get(userId);
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
