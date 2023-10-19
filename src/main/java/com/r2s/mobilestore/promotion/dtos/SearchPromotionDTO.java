package com.r2s.mobilestore.promotion.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Custom response status
 *
 * @author xuanmai
 * @since 2023-10-16
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchPromotionDTO {
    /**
     * Represents the discount code.
     */
    private String discountCode = "";

    /**
     * Represents the expiration date.
     */
    private String expireDate = null;

    /**
     * Represents discount available
     */
    private Boolean discountAvailable = null;

    /**
     * Represents min discount
     */
    private Integer minDiscount = 0;

    /**
     * Represents max discount
     */
    private Integer maxDiscount = 100;

    /**
     * Represents page
     */
    private PageDTO pageDTO;
}
