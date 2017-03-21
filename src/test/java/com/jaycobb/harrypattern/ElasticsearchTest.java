package com.jaycobb.harrypattern;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

public class ElasticsearchTest {

    private Client client;
    private SearchRequestBuilder bankSearch;

    @Before
    public void setup() throws UnknownHostException {
        client = TransportClient.builder().build().addTransportAddress(new InetSocketTransportAddress(
                InetAddress.getByName(Config.getProperty("elasticsearch.properties.host")),
                Integer.valueOf(Config.getProperty("elasticsearch.properties.port", "9300"))));
        bankSearch = client.prepareSearch("bank").setTypes("account")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
    }

    @Test
    public void testGet() throws UnknownHostException {
        final GetResponse response = client.prepareGet("bank", "account", "1").get();
        Assert.assertTrue(response != null);
    }

    @Test
    public void testBuilderSearch() throws UnknownHostException, ExecutionException, InterruptedException {
        final SearchResponse response = bankSearch.setQuery(QueryBuilders.matchQuery("state", "PA")).execute().get();
        Assert.assertTrue(response != null);
        Assert.assertTrue(response.getHits().totalHits() > 0);
        Assert.assertTrue(response.getHits().getHits()[0].getSource().get("state").toString().toLowerCase().contains("pa"));
    }

    @Test
    public void testJsonSearch() throws UnknownHostException, ExecutionException, InterruptedException {
        final SearchResponse response = bankSearch.setQuery("{\"query\": { \"match\": { \"state\": \"PA\" } }}")
                .execute().actionGet();
        Assert.assertTrue(response != null);
        Assert.assertTrue(response.getHits().totalHits() > 0);
        Assert.assertTrue(response.getHits().getHits()[0].getSource().get("state").toString().toLowerCase().contains("pa"));
    }

    @Test
    public void testXmlSearch() throws IOException, ExecutionException, InterruptedException {
        final String xml = "<query><match><state>PA</state></match></query>";
        final String json = new ObjectMapper().writeValueAsString(new XmlMapper().readTree(xml));
        final SearchResponse response = bankSearch.setQuery(json).execute().actionGet();
        Assert.assertTrue(response != null);
        Assert.assertTrue(response.getHits().totalHits() > 0);
        Assert.assertTrue(response.getHits().getHits()[0].getSource().get("state").toString().toLowerCase().contains("pa"));
    }

}
