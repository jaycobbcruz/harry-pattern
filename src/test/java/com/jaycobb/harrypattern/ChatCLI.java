package com.jaycobb.harrypattern;

import org.alicebot.ab.SearchContext;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ChatCLI {

    public static void main(String[] a) {
        SearchContext.strategy = new ElasticsearchStrategy(getClient());
        org.alicebot.ab.cli.Main.main(a);
    }

    private static Client getClient() {
        try {
            return TransportClient.builder().build().addTransportAddress(new InetSocketTransportAddress(
                    InetAddress.getByName(Config.getProperty("elasticsearch.properties.host")),
                    Integer.valueOf(Config.getProperty("elasticsearch.properties.port", "9300"))));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

}
