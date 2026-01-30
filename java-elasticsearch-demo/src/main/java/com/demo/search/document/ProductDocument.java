package com.demo.search.document;

import com.demo.search.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;

@Document(indexName = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String name;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String description;

    @Field(type = FieldType.Double)
    private BigDecimal price;

    @Field(type = FieldType.Keyword)
    private String gender;

    @Field(type = FieldType.Keyword)
    private String sillage;

    @Field(type = FieldType.Keyword)
    private String longevity;

    @Field(type = FieldType.Keyword)
    private String brand;

    @Field(type = FieldType.Keyword)
    private String concentration;

    public static ProductDocument fromEntity(Product product) {
        return ProductDocument.builder()
                .id(String.valueOf(product.getId()))
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .gender(product.getGender().name())
                .sillage(product.getSillage().name())
                .longevity(product.getLongevity().name())
                .brand(product.getBrand())
                .concentration(product.getConcentration())
                .build();
    }
}
