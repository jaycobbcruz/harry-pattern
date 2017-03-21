package com.jaycobb.harrypattern;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.alicebot.ab.Chat;
import org.alicebot.ab.SearchStrategy;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

class ElasticsearchStrategy implements SearchStrategy {

    private static final Logger log = LoggerFactory.getLogger(ElasticsearchStrategy.class);

    private final Client client;

    public ElasticsearchStrategy(final Client client) {
        this.client = client;
    }

    @Override
    public String process(Chat chat, String s) {
        try (XContentBuilder builder = XContentFactory.jsonBuilder()) {
            final Elasticrequest elasticrequest = new XmlMapper().readValue(s.trim(), Elasticrequest.class);
            final SearchResponse response = client.prepareSearch(elasticrequest.getIndex()).setTypes(elasticrequest.getType())
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(elasticrequest.getQuery())
                    .execute().actionGet();
            builder.startObject();
            response.toXContent(builder, ToXContent.EMPTY_PARAMS);
            return builder.string();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return s;
    }

}
