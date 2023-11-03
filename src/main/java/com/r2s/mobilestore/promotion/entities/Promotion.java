package com.r2s.mobilestore.promotion.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.r2s.mobilestore.utils.Constants.*;

/**
 * Represents a promotion
 *
 * @author xuanmai
 * @since 2023-10-03
 */
@Data
@Entity
@Table(name = "promotions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Represents the discount code.
     */
    @Column(name = "discount_code", nullable = false, unique = true)
    @Size(min = 0, max = 26, message = PROMOTION_CODE_MAX_MIN_CHARACTER)
    private String discountCode;

    /**
     * Represents description's discount
     */
    @Column(name = "campaign_description")
    @Size(min = 1, max = 2000, message = PROMOTION_MAX_MIN_CHARACTERS)
    private String campaignDescription;


    @Column(name = "used")
    private Integer used = 0;

    /**
     * represents the manufacture date
     */
    @Column(name = "manufacture_date")
    @FutureOrPresent(message = PROMOTION_DATE_FUTURE_OR_PRESENT)
    private LocalDate manufactureDate;

    /**
     * represents the expiration date
     */
    @Column(name = "expire_date")
    @FutureOrPresent(message = PROMOTION_DATE_FUTURE_OR_PRESENT)
    private LocalDate expireDate;

    /**
     * Represents the percentage discount
     */
    @Column(name = "percentage_discount")
    @Min(value = 1, message = PERCENTAGE_PROMOTION)
    @Max(value = 100, message = PERCENTAGE_PROMOTION)
    @NotNull(message = NOT_REQUIRE)
    private Double percentageDiscount;

    /**
     * Represents the maximum price discount
     */
    @Column(name = "maximum_price_discount")
    @Min(value = 1, message = MAXIMUM_PRICE_DISCOUNT)
    @Max(value = 999999, message = MAXIMUM_PRICE_DISCOUNT)
    @NotNull(message = NOT_REQUIRE)
    private Integer maximumPriceDiscount;

    /**
     * Represents the minimum order value to add a voucher
     */
    @Column(name = "minimum_order_value")
    @Min(value = 0, message = MINIMUM_ORDER_VALUE_PROMOTION)
    @Max(value = 999999, message = MINIMUM_ORDER_VALUE_PROMOTION)
    @NotNull(message = NOT_REQUIRE)
    private Integer minimumOrderValue;

    /**
     * Represents the status promotion
     */
    @Column(name = "status")
    private String status;

    /**
     * Represents the product’s images via product_images table.
     */
    @ElementCollection
    @CollectionTable(name = "customer_group_promotion", joinColumns = @JoinColumn(name = "promotion_id"))
    @Column(name = "name_customer_group")
    private List<String> customerGroup;

}
