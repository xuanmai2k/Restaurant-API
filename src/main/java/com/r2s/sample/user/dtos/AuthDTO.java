package com.r2s.sample.user.dtos;

import lombok.Data;

/**
 * This class is used to log in handling
 *
 * @author kyle
 * @since 2023-09-03
 */
@Data
public class AuthDTO {
    /**
     * Username to log in
     */
    private String username;

    /**
     * Password to log in
     */
    private String password;
}
