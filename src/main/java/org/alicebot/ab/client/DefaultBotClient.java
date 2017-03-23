package org.alicebot.ab.client;

import org.alicebot.ab.client.BotFactory;

public class DefaultBotClient implements BotClient {

    private org.alicebot.ab.client.BotFactory botFactory = new BotFactory();
    private ChatSession chatSession = new ChatSession(botFactory);


    @Override
    public void reloadBot(String botName) {
        botFactory.reloadBot(botName);
    }

    @Override
    public String chat(String userId, String message) {
        return chatSession.get(userId).multisentenceRespond(message);
    }

}
