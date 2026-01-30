package com.demo.search.service;

import com.demo.search.document.ProductDocument;
import com.demo.search.dto.CreateProductRequest;
import com.demo.search.dto.SearchRequest;
import com.demo.search.entity.Product;
import com.demo.search.entity.enums.Gender;
import com.demo.search.entity.enums.Longevity;
import com.demo.search.entity.enums.Sillage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class SearchServiceTest {

    @Container
    static ElasticsearchContainer elasticsearchContainer =
            new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:8.11.0")
                    .withEnv("xpack.security.enabled", "false");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.elasticsearch.uris", elasticsearchContainer::getHttpHostAddress);
    }

    @Autowired
    private SearchService searchService;

    @Autowired
    private ProductService productService;

    @Autowired
    private IndexService indexService;

    @BeforeEach
    void setUp() throws InterruptedException {
        Thread.sleep(1000);
    }

    @Test
    void testFuzzySearch() {
        CreateProductRequest request = CreateProductRequest.builder()
                .name("Sauvage Dior")
                .description("A fresh and powerful fragrance")
                .price(new BigDecimal("2500000"))
                .gender(Gender.MALE)
                .sillage(Sillage.STRONG)
                .longevity(Longevity.LONG_LASTING)
                .brand("Dior")
                .concentration("EDT")
                .build();

        Product product = productService.create(request);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SearchRequest searchRequest = SearchRequest.builder()
                .query("sauvaje")
                .build();

        List<ProductDocument> results = searchService.search(searchRequest);

        assertFalse(results.isEmpty(), "Should find product with typo");
        assertEquals("Sauvage Dior", results.get(0).getName());
    }

    @Test
    void testPriceRangeFilter() {
        productService.create(CreateProductRequest.builder()
                .name("Product A")
                .description("Affordable fragrance")
                .price(new BigDecimal("1500000"))
                .gender(Gender.UNISEX)
                .sillage(Sillage.MODERATE)
                .longevity(Longevity.MODERATE)
                .brand("Brand A")
                .build());

        productService.create(CreateProductRequest.builder()
                .name("Product B")
                .description("Premium fragrance")
                .price(new BigDecimal("2500000"))
                .gender(Gender.UNISEX)
                .sillage(Sillage.STRONG)
                .longevity(Longevity.LONG_LASTING)
                .brand("Brand B")
                .build());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SearchRequest searchRequest = SearchRequest.builder()
                .minPrice(new BigDecimal("2000000"))
                .maxPrice(new BigDecimal("3000000"))
                .build();

        List<ProductDocument> results = searchService.search(searchRequest);

        assertEquals(1, results.size());
        assertEquals("Product B", results.get(0).getName());
    }

    @Test
    void testGenderFilter() {
        productService.create(CreateProductRequest.builder()
                .name("Male Fragrance")
                .description("For men")
                .price(new BigDecimal("2000000"))
                .gender(Gender.MALE)
                .sillage(Sillage.STRONG)
                .longevity(Longevity.LONG_LASTING)
                .brand("Brand X")
                .build());

        productService.create(CreateProductRequest.builder()
                .name("Female Fragrance")
                .description("For women")
                .price(new BigDecimal("2000000"))
                .gender(Gender.FEMALE)
                .sillage(Sillage.MODERATE)
                .longevity(Longevity.MODERATE)
                .brand("Brand Y")
                .build());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SearchRequest searchRequest = SearchRequest.builder()
                .gender(Gender.MALE)
                .build();

        List<ProductDocument> results = searchService.search(searchRequest);

        assertEquals(1, results.size());
        assertEquals("Male Fragrance", results.get(0).getName());
    }

    @Test
    void testPrefixSearch() {
        productService.create(CreateProductRequest.builder()
                .name("Bleu de Chanel")
                .description("Woody aromatic fragrance")
                .price(new BigDecimal("3000000"))
                .gender(Gender.MALE)
                .sillage(Sillage.STRONG)
                .longevity(Longevity.LONG_LASTING)
                .brand("Chanel")
                .build());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SearchRequest searchRequest = SearchRequest.builder()
                .query("ble")
                .build();

        List<ProductDocument> results = searchService.search(searchRequest);

        assertFalse(results.isEmpty());
        assertTrue(results.get(0).getName().toLowerCase().startsWith("ble"));
    }
}
