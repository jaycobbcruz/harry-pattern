package com.jaycobb.harrypattern;

import org.alicebot.ab.SearchContext;
import org.alicebot.ab.client.BotClient;
import org.alicebot.ab.client.DefaultBotClient;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ElasticsearchStrategyTest {

    private static final Logger log = LoggerFactory.getLogger(ElasticsearchStrategyTest.class);

    private BotClient botClient = new DefaultBotClient();

    @Before
    public void setup() {
        SearchContext.strategy = new ElasticsearchStrategy(getClient());
    }

    @Test
    public void testSendMessage() {
        Assert.assertTrue(StringUtils.isNotBlank(botClient.chat("123", "SHOW ME ACCOUNTS WHERE STATE IS PA")));
    }

    private Client getClient() {
        try {
            return TransportClient.builder().build().addTransportAddress(new InetSocketTransportAddress(
                    InetAddress.getByName(Config.getProperty("elasticsearch.properties.host")),
                    Integer.valueOf(Config.getProperty("elasticsearch.properties.port", "9300"))));
        } catch (UnknownHostException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
