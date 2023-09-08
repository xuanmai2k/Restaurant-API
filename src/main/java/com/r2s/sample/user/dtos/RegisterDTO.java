package com.r2s.sample.user.dtos;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * This class is used to register a user
 *
 * @author kyle
 * @since 2023-09-03
 */
@Data
public class RegisterDTO {
    /**
     * Represents the username
     */
    private String username;

    /**
     * Represents the password
     */
    private String password;

    /**
     * Represents the mobile number
     */
    private String mobileNo;

    /**
     * Represents the email address
     */
    private String email;

    /**
     * Represents the city
     */
    private String city;

    /**
     * List of roles
     */
    Set<String> roles;
}
