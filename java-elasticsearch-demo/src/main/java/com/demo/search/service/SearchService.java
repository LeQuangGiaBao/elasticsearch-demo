package com.demo.search.service;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.demo.search.document.ProductDocument;
import com.demo.search.dto.SearchRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    public List<ProductDocument> search(SearchRequest request) {
        log.info("Executing search with query: {}", request.getQuery());

        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();
        List<Query> shouldQueries = new ArrayList<>();
        List<Query> filterQueries = new ArrayList<>();

        if (request.getQuery() != null && !request.getQuery().isBlank()) {
            String query = request.getQuery().toLowerCase();

            shouldQueries.add(Query.of(q -> q
                    .prefix(p -> p
                            .field("name")
                            .value(query)
                            .boost(3.0f)
                    )
            ));

            shouldQueries.add(Query.of(q -> q
                    .wildcard(w -> w
                            .field("name")
                            .value("*" + query + "*")
                            .boost(2.0f)
                    )
            ));

            shouldQueries.add(Query.of(q -> q
                    .fuzzy(f -> f
                            .field("name")
                            .value(query)
                            .fuzziness("AUTO")
                            .boost(1.0f)
                    )
            ));

            shouldQueries.add(Query.of(q -> q
                    .fuzzy(f -> f
                            .field("description")
                            .value(query)
                            .fuzziness("AUTO")
                            .boost(0.5f)
                    )
            ));
        }

        if (request.getMinPrice() != null) {
            filterQueries.add(Query.of(q -> q
                    .range(r -> r
                            .field("price")
                            .gte(co.elastic.clients.json.JsonData.of(request.getMinPrice()))
                    )
            ));
        }

        if (request.getMaxPrice() != null) {
            filterQueries.add(Query.of(q -> q
                    .range(r -> r
                            .field("price")
                            .lte(co.elastic.clients.json.JsonData.of(request.getMaxPrice()))
                    )
            ));
        }

        if (request.getGender() != null) {
            filterQueries.add(Query.of(q -> q
                    .term(t -> t
                            .field("gender")
                            .value(request.getGender().name())
                    )
            ));
        }

        if (request.getSillage() != null) {
            filterQueries.add(Query.of(q -> q
                    .term(t -> t
                            .field("sillage")
                            .value(request.getSillage().name())
                    )
            ));
        }

        if (request.getLongevity() != null) {
            filterQueries.add(Query.of(q -> q
                    .term(t -> t
                            .field("longevity")
                            .value(request.getLongevity().name())
                    )
            ));
        }

        if (request.getBrand() != null && !request.getBrand().isBlank()) {
            filterQueries.add(Query.of(q -> q
                    .term(t -> t
                            .field("brand")
                            .value(request.getBrand())
                    )
            ));
        }

        if (!shouldQueries.isEmpty()) {
            boolQueryBuilder.should(shouldQueries);
            boolQueryBuilder.minimumShouldMatch("1");
        }

        if (!filterQueries.isEmpty()) {
            boolQueryBuilder.filter(filterQueries);
        }

        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 20;
        Pageable pageable = PageRequest.of(page, size);

        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(boolQueryBuilder.build()._toQuery())
                .withPageable(pageable)
                .build();

        SearchHits<ProductDocument> searchHits = elasticsearchOperations.search(
                searchQuery,
                ProductDocument.class
        );

        log.info("Search returned {} results", searchHits.getTotalHits());

        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .toList();
    }
}
