package org.alicebot.ab;

import org.alicebot.ab.Chat;

public interface SearchStrategy {

    String process(Chat chatSession, String content);

}
