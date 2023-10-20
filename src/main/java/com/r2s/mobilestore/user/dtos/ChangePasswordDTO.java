package com.r2s.mobilestore.user.dtos;

import lombok.Data;

/**
 * This class is used to change password
 *
 * @author KhanhBD
 * @since 2023-10-09
 */
@Data
public class ChangePasswordDTO {
    private String oldPassword;
    private String newPassword;
    private String email;
    private String otpCode;
}
