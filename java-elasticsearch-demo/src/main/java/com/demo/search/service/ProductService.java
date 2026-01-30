package com.demo.search.service;

import com.demo.search.dto.CreateProductRequest;
import com.demo.search.dto.UpdateProductRequest;
import com.demo.search.entity.Product;
import com.demo.search.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final IndexService indexService;

    @Transactional
    public Product create(CreateProductRequest request) {
        log.info("Creating new product: {}", request.getName());

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .gender(request.getGender())
                .sillage(request.getSillage())
                .longevity(request.getLongevity())
                .brand(request.getBrand())
                .concentration(request.getConcentration())
                .build();

        Product savedProduct = productRepository.save(product);

        indexService.indexProduct(savedProduct);

        log.info("Product created and indexed with ID: {}", savedProduct.getId());
        return savedProduct;
    }

    @Transactional
    public Product update(Long id, UpdateProductRequest request) {
        log.info("Updating product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        if (request.getGender() != null) {
            product.setGender(request.getGender());
        }
        if (request.getSillage() != null) {
            product.setSillage(request.getSillage());
        }
        if (request.getLongevity() != null) {
            product.setLongevity(request.getLongevity());
        }
        if (request.getBrand() != null) {
            product.setBrand(request.getBrand());
        }
        if (request.getConcentration() != null) {
            product.setConcentration(request.getConcentration());
        }

        Product updatedProduct = productRepository.save(product);

        indexService.indexProduct(updatedProduct);

        log.info("Product updated and re-indexed with ID: {}", updatedProduct.getId());
        return updatedProduct;
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        productRepository.delete(product);

        indexService.deleteProduct(String.valueOf(id));

        log.info("Product deleted from database and search index: {}", id);
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }
}
