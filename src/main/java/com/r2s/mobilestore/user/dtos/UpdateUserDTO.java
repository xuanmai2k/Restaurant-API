package com.r2s.mobilestore.user.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateUserDTO {
    private String fullName;
    private String username;
    private String email;
    private String gender;
    private LocalDate dateOfBirth;
}
