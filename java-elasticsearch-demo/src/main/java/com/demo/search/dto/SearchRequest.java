package com.demo.search.dto;

import com.demo.search.entity.enums.Gender;
import com.demo.search.entity.enums.Longevity;
import com.demo.search.entity.enums.Sillage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {
    private String query;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Gender gender;
    private Sillage sillage;
    private Longevity longevity;
    private String brand;
    private Integer page;
    private Integer size;
}
