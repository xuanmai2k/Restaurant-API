package com.r2s.mobilestore.promotion.dtos;

import com.r2s.mobilestore.dtos.PageDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
     * Represents the status of promotion.
     */
    private String status = "";

    /**
     * Represents customer group
     */
    private String customerGroup = "";

    /**
     * Represents the before or after date.
     */
    private Boolean isBeforeManufactureDate = true;

    /**
     * Represents the manufacture date.
     */
    private LocalDate manufactureDate;

    /**
     * Represents the calculation of used.
     */
    private String compareUsed;

    /**
     * Represents the client used
     */
    private Integer used;

    /**
     * Represents page
     */
    private PageDTO pageDTO;
}
