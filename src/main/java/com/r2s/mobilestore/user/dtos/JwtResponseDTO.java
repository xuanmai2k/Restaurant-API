package com.r2s.mobilestore.user.dtos;

import com.r2s.mobilestore.user.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class JwtResponseDTO {
    private String token;
    private String type;
    private String email;
    private Set<Role> roles;
}