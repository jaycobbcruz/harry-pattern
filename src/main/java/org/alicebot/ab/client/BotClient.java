package org.alicebot.ab.client;

public interface BotClient {

    void reloadBot(final String botName);

    String chat(final String botName, final String message);

}
