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
     * Represents the minimum order value to add a voucher
     */
    @Column(name = "total_purchase", nullable = false)
    @Min(value = 0, message = PROMOTION_NON_NEGATIVE)
    private Integer totalPurchase;

    /**
     * Represents the percentage discount
     */
    @Column(name = "discount", nullable = false)
    @Min(value = 0, message = PROMOTION_NON_NEGATIVE)
    @Max(value = 100, message = PROMOTION_MAX_VALUE)
    private Double discount;

    /**
     * represents the maximum amount of reduction
     */
    @Column(name = "maximum_promotion_get", nullable = false)
    @Min(value = 0, message = PROMOTION_NON_NEGATIVE)
    private Integer maxPromotionGet;

    /**
     * represents the expiration date
     */
    @Column(name = "expire_date", nullable = false)
    @FutureOrPresent(message = PROMOTION_DATE_FUTURE_OR_PRESENT)
    private LocalDate expireDate;

    /**
     * Represents description's discount
     */
    @Column(name = "campaign_description", nullable = false)
    @Size(min = 1, max = 1000, message = PROMOTION_MAX_MIN_CHARACTERS)
    private String campaignDescription;

    /**
     * Represents discount available
     */
    @Column(name = "discount_available", nullable = false)
    private Boolean discountAvailable = true;


}
