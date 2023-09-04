package com.r2s.sample.user.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the user
 *
 * @author kyle
 * @since 2023-09-02
 */
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Represents the username
     */
    @Column(name = "user_name", unique = true, nullable = false, length = 50)
    private String username;

    /**
     * Represents the mobile number
     */
    @Column(name = "mobile_no", length = 20)
    private String mobileNo;

    /**
     * Represents the email address
     */
    @Column(name = "email_address", length = 50)
    private String emailId;

    /**
     * Represents the city
     */
    @Column(length = 100)
    private String city;

    /**
     * Represents the password
     */
    @Column(length = 64)
    private String password;
}