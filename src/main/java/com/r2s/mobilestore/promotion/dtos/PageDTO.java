package com.r2s.mobilestore.promotion.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Custom response status
 *
 * @author xuanmai
 * @since 2023-10-19
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageDTO {
    /**
     * Represents the number of page.
     */
    private Integer pageNumber;

    /**
     * Represents the size of page.
     */
    private Integer pageSize;
}
