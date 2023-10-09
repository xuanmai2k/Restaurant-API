package com.r2s.mobilestore.user.dtos;

import com.r2s.mobilestore.user.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

/**
 * Represents jwt response
 *
 * @author KhanhBD
 * @since 2023-10-03
 */
@Data
@AllArgsConstructor
public class JwtResponseDTO {
    private String token;
    private String type;
    private String email;
    private Set<Role> roles;
}