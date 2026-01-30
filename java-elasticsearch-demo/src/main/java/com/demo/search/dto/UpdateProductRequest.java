package com.demo.search.dto;

import com.demo.search.entity.enums.Gender;
import com.demo.search.entity.enums.Longevity;
import com.demo.search.entity.enums.Sillage;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {

    private String name;
    private String description;

    @Positive(message = "Price must be positive")
    private BigDecimal price;

    private Gender gender;
    private Sillage sillage;
    private Longevity longevity;
    private String brand;
    private String concentration;
}
