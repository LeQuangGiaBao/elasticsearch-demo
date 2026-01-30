package com.demo.search.service;

import com.demo.search.dto.CreateProductRequest;
import com.demo.search.dto.UpdateProductRequest;
import com.demo.search.entity.Product;
import com.demo.search.entity.enums.Gender;
import com.demo.search.entity.enums.Longevity;
import com.demo.search.entity.enums.Sillage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class ProductServiceTest {

    @Container
    static PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    private ProductService productService;

    @Test
    void testCreateProduct() {
        CreateProductRequest request = CreateProductRequest.builder()
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("1000000"))
                .gender(Gender.UNISEX)
                .sillage(Sillage.MODERATE)
                .longevity(Longevity.MODERATE)
                .brand("Test Brand")
                .concentration("EDP")
                .build();

        Product product = productService.create(request);

        assertNotNull(product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals(new BigDecimal("1000000"), product.getPrice());
    }

    @Test
    void testUpdateProduct() {
        CreateProductRequest createRequest = CreateProductRequest.builder()
                .name("Original Name")
                .description("Original Description")
                .price(new BigDecimal("1000000"))
                .gender(Gender.MALE)
                .sillage(Sillage.MODERATE)
                .longevity(Longevity.MODERATE)
                .brand("Original Brand")
                .build();

        Product product = productService.create(createRequest);

        UpdateProductRequest updateRequest = UpdateProductRequest.builder()
                .name("Updated Name")
                .price(new BigDecimal("1500000"))
                .build();

        Product updatedProduct = productService.update(product.getId(), updateRequest);

        assertEquals("Updated Name", updatedProduct.getName());
        assertEquals(new BigDecimal("1500000"), updatedProduct.getPrice());
        assertEquals("Original Description", updatedProduct.getDescription());
    }

    @Test
    void testDeleteProduct() {
        CreateProductRequest request = CreateProductRequest.builder()
                .name("To Be Deleted")
                .description("This will be deleted")
                .price(new BigDecimal("1000000"))
                .gender(Gender.FEMALE)
                .sillage(Sillage.INTIMATE)
                .longevity(Longevity.WEAK)
                .brand("Delete Brand")
                .build();

        Product product = productService.create(request);
        Long productId = product.getId();

        productService.delete(productId);

        assertThrows(RuntimeException.class, () -> productService.findById(productId));
    }
}
