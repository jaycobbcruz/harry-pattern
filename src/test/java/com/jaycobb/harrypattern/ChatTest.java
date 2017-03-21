package com.jaycobb.harrypattern;

import org.alicebot.ab.client.BotClient;
import org.alicebot.ab.client.DefaultBotClient;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ChatTest {

    final BotClient botClient = new DefaultBotClient();

    @Before
    public void prepare() {
        botClient.reloadBot("super");
    }

    @Test
    public void testSendMessage() {
        Assert.assertTrue(StringUtils.isNotBlank(botClient.chat("123", "hello")));
    }

}
