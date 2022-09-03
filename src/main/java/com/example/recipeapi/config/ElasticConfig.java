package com.example.recipeapi.config;

import joptsimple.internal.Strings;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.Arrays;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.example.recipeapi.repository")
public class ElasticConfig extends AbstractElasticsearchConfiguration {

    @Value("${elasticsearch.host}")
    private String eHost;

    @Value("${elasticsearch.port}")
    private String ePort;

    @Bean
    public RestHighLevelClient elasticsearchClient() {

        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(Strings.join(Arrays.asList(eHost, ":", ePort), ""))
                .build();

        return RestClients.create(clientConfiguration).rest();
    }

    @Qualifier("elsTemp")
    @Bean
    public ElasticsearchRestTemplate elsTemplate() {
        return new ElasticsearchRestTemplate(elasticsearchClient());
    }
}
