package com.r2s.mobilestore.promotion.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

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
    private Integer totalPurchase;

    /**
     * Represents the percentage discount
     */
    @Column(name = "discount", nullable = false)
    @Min(value = 0, message = "Max promotion get must be a non-negative number")
    @Max(value = 100, message = "Max promotion get must not exceed 100")
    private Double discount;

    /**
     * represents the maximum amount of reduction
     */
    @Column(name = "maximum_promotion_get", nullable = false)
    @Min(value = 0, message = "Max promotion get must be a non-negative number")
    private Integer maxPromotionGet;

    /**
     * represents the expiration date
     */
    @Column(name = "expire_date", nullable = false)
    @Future(message = "Expire date must be in the future")
    private LocalDate expireDate;

    /**
     * Represents description's discount
     */
    @Column(name = "campaign_description", nullable = false)
    @Size(min = 1, max = 1000, message = "Must be from 1 to 1000 characters")
    private String campaignDescription;

    /**
     * Represents discount available
     */
    @Column(name = "discount_available", nullable = false)
    private Boolean discountAvailable = true;

}
