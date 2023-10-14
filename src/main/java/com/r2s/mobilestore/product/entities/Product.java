package com.r2s.mobilestore.product.entities;

import com.r2s.mobilestore.category.entities.Category;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Represents a product
 *
 * @author xuanmai
 * @since 2023-10-06
 */
@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Represents the product’s code.
     */
    @Column(name = "product_code")
    private String productCode;

    /**
     * Represents the product’s name.
     */
    @Column(name = "name", nullable = false)
    private String name;


    /**
     * Represents the product’s price.
     */
    @Column(name = "price", nullable = false)
    private Double price;

    /**
     * Represents the product’s quantity.
     */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /**
     * Represents the product’s description.
     */
    @Column(name = "description", nullable = false)
    private String description;

    /**
     * Represents the product’s memory.
     */
    @Column(name = "memory_types", nullable = false)
    private String memoryTypes;

    /**
     * Represents the product’s color.
     */
    @Column(name = "color", nullable = false)
    private String color;

    /**
     * Represents the product’s condition.
     */
    @Column(name = "product_condition", nullable = false)
    private String productCondition;

    /**
     * Represents the product’s manufacturer.
     */
    @ManyToOne
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Manufacturer manufacturer;

    /**
     * Represents the product’s category.
     */
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /**
     * Represents the product’s images via product_images table.
     */
    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> images;

}
