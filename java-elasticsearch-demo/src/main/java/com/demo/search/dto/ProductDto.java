package com.demo.search.dto;

import com.demo.search.entity.Product;
import com.demo.search.entity.enums.Gender;
import com.demo.search.entity.enums.Longevity;
import com.demo.search.entity.enums.Sillage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Gender gender;
    private Sillage sillage;
    private Longevity longevity;
    private String brand;
    private String concentration;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProductDto fromEntity(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .gender(product.getGender())
                .sillage(product.getSillage())
                .longevity(product.getLongevity())
                .brand(product.getBrand())
                .concentration(product.getConcentration())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
