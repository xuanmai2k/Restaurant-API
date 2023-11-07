package com.r2s.mobilestore.product.entities;

import com.r2s.mobilestore.category.entities.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

import static com.r2s.mobilestore.utils.Constants.*;

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
@Table(name = "products", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"product_code"})
})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Represents the product’s code.
     */
    @Column(name = "product_code", unique = true)
    private String productCode;

    /**
     * Represents the product’s name.
     */
    @Column(name = "name", nullable = false)
    @NotBlank(message = NOT_REQUIRE)
    @Size(max = 255, message = PRODUCT_NAME_MAX_CHARACTERS)
    private String name;


    /**
     * Represents the product’s price.
     */
    @Column(name = "price", nullable = false)
    @NotNull(message = NOT_REQUIRE)
    @DecimalMin(value = "0", inclusive = true, message = PRODUCT_PRICE_MIN_VALUE)
    @DecimalMax(value = "999999.99", inclusive = true, message = PRODUCT_PRICE_MAX_VALUE)
    private Double price;

    /**
     * Represents the product’s discount price.
     */
    @Column(name = "discount_price")
    @DecimalMin(value = "0", inclusive = true, message = PRODUCT_PRICE_MIN_VALUE)
    @DecimalMax(value = "999999.99", inclusive = true, message = PRODUCT_PRICE_MAX_VALUE)
    private Double discountPrice;

    /**
     * Represents the product’s quantity.
     */
    @Column(name = "quantity", nullable = false)
    @NotNull(message = NOT_REQUIRE)
    @Min(value = 0, message = PRODUCT_QUANTITY_MIN_VALUE)
    @Max(value = 999999, message = PRODUCT_QUANTITY_MAX_VALUE)
    private Integer quantity;

    /**
     * Represents the product’s description.
     */
    @Column(name = "description", nullable = false)
    @NotBlank(message = NOT_REQUIRE)
    @Size(min = 1, max = 1000, message = PRODUCT_MAX_MIN_CHARACTERS)
    private String description;

    /**
     * Represents the product’s memory.
     */
    @Column(name = "memory_types", nullable = false)
    @NotBlank(message = NOT_REQUIRE)
    @Size(min = 1, max = 1000, message = PRODUCT_MAX_MIN_CHARACTERS)
    private String memoryTypes;

    /**
     * Represents the product’s color.
     */
    @Column(name = "color", nullable = false)
    @NotBlank(message = NOT_REQUIRE)
    @Size(min = 1, max = 1000, message = PRODUCT_MAX_MIN_CHARACTERS)
    private String color;

    /**
     * Represents the product’s condition.
     */
    @Column(name = "product_condition", nullable = false)
    @NotBlank(message = NOT_REQUIRE)
    @Size(min = 1, max = 20, message = PRODUCT_CONDITION_MAX_MIN_CHARACTERS)
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
