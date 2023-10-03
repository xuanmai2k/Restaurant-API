package com.r2s.mobilestore.user.dtos;

import lombok.Data;


/**
 * This class is used to register a user
 *
 * @author kyle
 * @since 2023-09-03
 */
@Data
public class RegisterDTO {
    /**
     * Represents the email
     */
    private String email;

    /**
     * Represents the password
     */
    private String username;

    /**
     * Represents the mobile number
     */
    private String password;

    /**
     * Represents the email address
     */
    private String rePassword;
}

