package com.demo.search.controller;

import com.demo.search.dto.*;
import com.demo.search.entity.Product;
import com.demo.search.entity.enums.Gender;
import com.demo.search.entity.enums.Longevity;
import com.demo.search.entity.enums.Sillage;
import com.demo.search.service.IndexService;
import com.demo.search.service.ProductService;
import com.demo.search.service.SearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final SearchService searchService;
    private final IndexService indexService;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody CreateProductRequest request) {
        log.info("Received request to create product: {}", request.getName());
        Product product = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductDto.fromEntity(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        log.info("Received request to get product: {}", id);
        Product product = productService.findById(id);
        return ResponseEntity.ok(ProductDto.fromEntity(product));
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        log.info("Received request to get all products");
        List<Product> products = productService.findAll();
        List<ProductDto> productDtos = products.stream()
                .map(ProductDto::fromEntity)
                .toList();
        return ResponseEntity.ok(productDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request) {
        log.info("Received request to update product: {}", id);
        Product product = productService.update(id, request);
        return ResponseEntity.ok(ProductDto.fromEntity(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("Received request to delete product: {}", id);
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProducts(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) Sillage sillage,
            @RequestParam(required = false) Longevity longevity,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size) {

        log.info("Received search request with query: {}", query);

        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .gender(gender)
                .sillage(sillage)
                .longevity(longevity)
                .brand(brand)
                .page(page)
                .size(size)
                .build();

        List<ProductDto> results = searchService.search(searchRequest).stream()
                .map(doc -> ProductDto.builder()
                        .id(Long.parseLong(doc.getId()))
                        .name(doc.getName())
                        .description(doc.getDescription())
                        .price(doc.getPrice())
                        .gender(Gender.valueOf(doc.getGender()))
                        .sillage(Sillage.valueOf(doc.getSillage()))
                        .longevity(Longevity.valueOf(doc.getLongevity()))
                        .brand(doc.getBrand())
                        .concentration(doc.getConcentration())
                        .build())
                .toList();

        return ResponseEntity.ok(results);
    }

    @PostMapping("/reindex")
    public ResponseEntity<ReindexResponse> reindexAll() {
        log.info("Received request to reindex all products");

        List<Product> allProducts = productService.findAll();
        indexService.reindexAll(allProducts);

        ReindexResponse response = ReindexResponse.builder()
                .indexedCount(allProducts.size())
                .status("SUCCESS")
                .message("Successfully reindexed " + allProducts.size() + " products")
                .build();

        return ResponseEntity.ok(response);
    }
}
