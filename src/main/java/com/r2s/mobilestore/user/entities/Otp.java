package com.r2s.mobilestore.user.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Represents the Otp
 *
 * @author KhanhBD
 * @since 2023-10-03
 */
@Entity
@Table(name = "otp")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "otp_code")
    private String otpCode;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "expiration_time")
    private Date expirationTime;

}
