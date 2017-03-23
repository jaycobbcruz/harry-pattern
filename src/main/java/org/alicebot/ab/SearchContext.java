package org.alicebot.ab;

import org.alicebot.ab.SearchStrategy;

public class SearchContext {

    public static SearchStrategy strategy;

    public static String process(Chat chatSession, String content) {
        return strategy.process(chatSession, content);
    }
}
