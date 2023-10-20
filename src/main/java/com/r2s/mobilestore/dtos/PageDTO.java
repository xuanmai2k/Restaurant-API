package com.r2s.mobilestore.dtos;

import lombok.Data;

/**
 * Represents page
 *
 * @author KhanhBD
 * @since 2023-10-19
 */
@Data
public class PageDTO {
    private int pageSize = 10;
    private int pageNumber = 0;
}
