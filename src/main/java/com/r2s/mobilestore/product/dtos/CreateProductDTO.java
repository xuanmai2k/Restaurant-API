package com.r2s.mobilestore.product.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Custom response status
 *
 * @author xuanmai
 * @since 2023-10-06
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateProductDTO {

    /**
     * Represents the product’s code.
     */
    private String productCode;

    /**
     * Represents the response name.
     */
    private String name;

    /**
     * Represents the response price.
     */
    private Double price;

    /**
     * Represents the response quantity.
     */
    private Integer quantity;

    /**
     * Represents the response description.
     */
    private String description;

    /**
     * Represents the response types of ROM.
     */
    private String memoryTypes;

    /**
     * Represents the response color.
     */
    private String color;

    /**
     * Represents the response condition.
     */
    private String productCondition;

    /**
     * Represents the response category.
     */
    private String category;

    /**
     * Represents the response manufacturer.
     */
    private String manufacturer;

    /**
     * Represents the response images.
     */
    private List<MultipartFile> images;
}
