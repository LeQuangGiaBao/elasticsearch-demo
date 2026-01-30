package com.demo.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.demo.search.repository")
@EnableElasticsearchRepositories(basePackages = "com.demo.search.repository")
public class SearchDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchDemoApplication.class, args);
    }
}
