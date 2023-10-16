package com.r2s.mobilestore.product.entities;

import com.r2s.mobilestore.category.entities.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @NotBlank(message = "Name is required.")
    @Size(max = 255, message = "Name cannot exceed 255 characters.")
    private String name;


    /**
     * Represents the product’s price.
     */
    @Column(name = "price", nullable = false)
    @NotNull(message = "Price is required.")
    @DecimalMin(value = "0", inclusive = true, message = "Price must be greater than or equal to 0.")
    @DecimalMax(value = "999999.99", inclusive = true, message = "Price cannot exceed 999999.99.")
    private Double price;

    /**
     * Represents the product’s quantity.
     */
    @Column(name = "quantity", nullable = false)
    @NotNull(message = "Quantity is required.")
    @Min(value = 0, message = "Quantity must be at least 0.")
    @Max(value = 999999, message = "Quantity cannot exceed 999999.")
    private Integer quantity;

    /**
     * Represents the product’s description.
     */
    @Column(name = "description", nullable = false)
    @NotBlank(message = "Description is required.")
    @Size(min = 1, max = 1000, message = "Description must be from 1 to 1000 characters.")
    private String description;

    /**
     * Represents the product’s memory.
     */
    @Column(name = "memory_types", nullable = false)
    @NotBlank(message = "Memory types is required.")
    @Size(min = 1, max = 1000, message = "Memory types must be from 1 to 1000 characters.")
    private String memoryTypes;

    /**
     * Represents the product’s color.
     */
    @Column(name = "color", nullable = false)
    @NotBlank(message = "Color is required.")
    @Size(min = 1, max = 1000, message = "Color must be from 1 to 1000 characters.")
    private String color;

    /**
     * Represents the product’s condition.
     */
    @Column(name = "product_condition", nullable = false)
    @NotBlank(message = "Product condition is required.")
    @Size(min = 1, max = 20, message = "Product condition must be from 1 to 20 characters.")
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
