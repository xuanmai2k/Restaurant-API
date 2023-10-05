package com.r2s.mobilestore.user.dtos;

import lombok.Data;


/**
 * This class is used to register a user
 *
 * @author KhanhBD
 * @since 2023-10-04
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
    private String fullName;

    /**
     * Represents the mobile number
     */
    private String password;

    /**
     * Represents the otp code
     */
    private String otpCode;
}

