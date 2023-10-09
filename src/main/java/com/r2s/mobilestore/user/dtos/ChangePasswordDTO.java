package com.r2s.mobilestore.user.dtos;

import lombok.Data;

@Data
public class ChangePasswordDTO {
    private String oldPassword;
    private String newPassword;
    private String email;
    private String otpCode;
}
