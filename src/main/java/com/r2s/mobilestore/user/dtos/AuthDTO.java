package com.r2s.mobilestore.user.dtos;

import lombok.Data;

/**
 * Represents login feature
 *
 * @author KhanhBD
 * @since 2023-10-03
 */
@Data
public class AuthDTO {
    /**
     * Username to log in
     */
    private String email;

    /**
     * Password to log in
     */
    private String password;
}
