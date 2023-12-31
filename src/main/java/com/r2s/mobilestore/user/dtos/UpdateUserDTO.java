package com.r2s.mobilestore.user.dtos;

import lombok.Data;

import java.time.LocalDate;

/**
 * This class is used to update User
 *
 * @author KhanhBD
 * @since 2023-10-09
 */
@Data
public class UpdateUserDTO {
    private String fullName;
    private String phoneNumber;
    private String gender;
    private LocalDate dateOfBirth;
}
