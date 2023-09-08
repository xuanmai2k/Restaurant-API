package com.r2s.sample.user.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * This class is JWT response
 *
 * @author kyle
 * @since 2029-09-07
 */
@Data
@AllArgsConstructor
public class JwtResponseDTO {
    private String token;
    private String type;
    private String email;
    private List<String> roles;
}