package com.r2s.mobilestore.user.dtos;

import lombok.Data;

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
