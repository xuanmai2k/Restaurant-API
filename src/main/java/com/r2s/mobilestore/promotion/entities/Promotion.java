package com.r2s.mobilestore.promotion.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

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
    private String discountCode;

    /**
     * Represents description's discount
     */
    @Column(name = "campaign_description")
//    @Size(min = 1, max = 1000, message = PROMOTION_MAX_MIN_CHARACTERS)
    private String campaignDescription;


    @Column(name = "used")
    private Integer used = 0;

    /**
     * represents the manufacture date
     */
    @Column(name = "manufacture_date")
    private LocalDate manufactureDate;

    /**
     * represents the expiration date
     */
    @Column(name = "expire_date")
//    @FutureOrPresent(message = PROMOTION_DATE_FUTURE_OR_PRESENT)
    private LocalDate expireDate;

    /**
     * Represents the percentage discount
     */
    @Column(name = "percentage_discount")
//    @Min(value = 0, message = PROMOTION_NON_NEGATIVE)
//    @Max(value = 100, message = PROMOTION_MAX_VALUE)
    private Double percentageDiscount;

    /**
     * Represents the maximum price discount
     */
    @Column(name = "maximum_price_discount")
//    @Min(value = 0, message = PROMOTION_NON_NEGATIVE)
    private Integer maximumPriceDiscount;

    /**
     * Represents the minimum order value to add a voucher
     */
    @Column(name = "minimum_order_value")
//    @Min(value = 0, message = PROMOTION_NON_NEGATIVE)
    private Integer minimumOrderValue;

    /**
     * Represents the status promotion
     */
    @Column(name = "status")
    private String status;

    /**
     * Represents the customer group
     */
    @Column(name = "customer_group")
    private String customerGroup;

}
