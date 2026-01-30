package com.demo.search.service;

import com.demo.search.document.ProductDocument;
import com.demo.search.entity.Product;
import com.demo.search.repository.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class IndexService {

    private final ProductSearchRepository searchRepository;

    public void indexProduct(Product product) {
        try {
            ProductDocument document = ProductDocument.fromEntity(product);
            searchRepository.save(document);
            log.info("Successfully indexed product: {}", product.getId());
        } catch (Exception e) {
            log.error("Failed to index product: {}", product.getId(), e);
        }
    }

    public void deleteProduct(String id) {
        try {
            searchRepository.deleteById(id);
            log.info("Successfully deleted product from index: {}", id);
        } catch (Exception e) {
            log.error("Failed to delete product from index: {}", id, e);
        }
    }

    public void reindexAll(List<Product> products) {
        log.info("Starting reindex of {} products", products.size());

        searchRepository.deleteAll();

        List<ProductDocument> documents = products.stream()
                .map(ProductDocument::fromEntity)
                .toList();

        searchRepository.saveAll(documents);

        log.info("Reindex completed. Indexed {} products", products.size());
    }
}
