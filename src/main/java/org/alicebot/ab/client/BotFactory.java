package org.alicebot.ab.client;

import org.alicebot.ab.Bot;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.config.Config;

import java.util.HashMap;
import java.util.Map;

public class BotFactory {

	public static final String DEFAULT_BOT = "super";
	private Map<String, Bot> bots = new HashMap<>();

    static {
        MagicStrings.root_path = Config.getProperty("ab.root.path");
    }

	public Bot get(String botName) {
		if (bots.containsKey(botName)) {
			return bots.get(botName);
		} else if (!bots.containsKey(DEFAULT_BOT)) {
			bots.put(DEFAULT_BOT, new Bot(botName, MagicStrings.root_path, "chat"));
		}
		return bots.get(DEFAULT_BOT);
	}

	public void put(String botName, Bot bot) {
		bots.put(botName, bot);
	}

	public Bot reloadBot(final String botName) {
		bots.put(botName, new Bot(botName, MagicStrings.root_path, "reload"));
		return bots.get(botName);
	}
}
